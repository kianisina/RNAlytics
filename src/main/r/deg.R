library(plumber)
library(readr)

#* @apiTitle Bioinformatics Plotting API

#* Generate a Volcano Plot from uploaded CSV data
#* @post /volcano
#* @serializer png
function(req, res) {

  raw_data <- req$postBody

  message("--- New Request Received ---")
  message("Data length: ", nchar(raw_data), " characters")

  tryCatch({

    # 🔥 1. Normalize line endings
    raw_data <- gsub("\r", "", raw_data)

    # 🔥 2. Fix broken quotes globally (safe approach)
    raw_data <- gsub('""', '"', raw_data)

    # 🔥 3. Try parsing with readr (robust)
    df <- read_csv(
      raw_data,
      show_col_types = FALSE,
      progress = FALSE,
      guess_max = 10000
    )

    # 🔍 Debug
    message("Columns detected: ", paste(colnames(df), collapse = ", "))
    message("Dimensions: ", paste(dim(df), collapse = " x "))

    # 🔥 4. Dynamically find required columns
    col_logfc <- grep("log2.*fold", colnames(df), ignore.case = TRUE, value = TRUE)
    col_pval  <- grep("^pvalue$|p.*value", colnames(df), ignore.case = TRUE, value = TRUE)

    if (length(col_logfc) == 0) stop("log2FoldChange column not found")
    if (length(col_pval) == 0) stop("pvalue column not found")

    # Take first match
    col_logfc <- col_logfc[1]
    col_pval  <- col_pval[1]

    message("Using columns: ", col_logfc, " and ", col_pval)

    # 🔥 5. Convert safely to numeric
    logFC_data <- suppressWarnings(as.numeric(df[[col_logfc]]))
    pval_data  <- suppressWarnings(as.numeric(df[[col_pval]]))

    # Remove invalid rows
    valid <- !is.na(logFC_data) & !is.na(pval_data) & pval_data > 0

    logFC_data <- logFC_data[valid]
    pval_data  <- pval_data[valid]

    if (length(logFC_data) == 0) {
      stop("No valid numeric data found after cleaning")
    }

    # 🔥 6. Compute volcano values
    negLog10Pval <- -log10(pval_data)

    # 🔥 7. Dynamic thresholding
    point_colors <- ifelse(
      negLog10Pval > -log10(0.05) & abs(logFC_data) > 1,
      "red",
      "black"
    )

    # 🔥 8. Plot
    plot(
      logFC_data,
      negLog10Pval,
      main = "Volcano Plot",
      xlab = "Log2 Fold Change",
      ylab = "-Log10(p-value)",
      pch = 16,
      col = point_colors
    )

    message("Plot generated successfully!")

  }, error = function(e) {

    message("CRASH IN R: ", e$message)
    res$status <- 500
    return(list(error = e$message))

  })
}
