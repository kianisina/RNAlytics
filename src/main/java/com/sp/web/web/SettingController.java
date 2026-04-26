package com.sp.web.web;

import com.sp.web.setting.Setting;
import com.sp.web.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("/corporateDesign")
    public ResponseEntity<?> getCorporateDesign() {
        return settingService.getSetting()
            .map(setting -> ResponseEntity.ok().body(setting))
            .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @PostMapping("/saveSettings/{id}")
    public ResponseEntity<?> saveSettings(
        @PathVariable String id,
        @RequestParam(value = "file", required = false) MultipartFile file,
        @RequestParam("companyName") String companyName,
        @RequestParam("fontColor") String fontColor,
        @RequestParam("backgroundColor") String backgroundColor,
        @RequestParam("imprintText") String imprintText
    ) throws IOException {
        
        // 1. Fetch the existing setting. If it doesn't exist yet, create a new one.
        Setting setting = settingService.getSetting().orElse(new Setting());
        
        // 2. Update the text fields
        setting.setId(id); 
        setting.setCompanyName(companyName);
        setting.setBackgroundColor(backgroundColor);
        setting.setFontColor(fontColor);
        setting.setImprintText(imprintText);

        // 3. ONLY overwrite the logo if the user actually uploaded a new one
        if (file != null && !file.isEmpty()) {
            setting.setLogo(file.getBytes());
        }

        // 4. Save it back to the database
        settingService.saveSetting(setting);
        
        return ResponseEntity.ok().build();
    }
}