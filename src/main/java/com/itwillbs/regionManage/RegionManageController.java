package com.itwillbs.regionManage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class RegionManageController {

    private final RegionManageService regionManageService;

    @GetMapping("/admin/region/init")
    @ResponseBody
    public String initRegionData() {
        regionManageService.initRegionData();
        return "region init success";
    }
}
