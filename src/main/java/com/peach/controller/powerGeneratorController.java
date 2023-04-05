package com.peach.controller;

import com.peach.entity.powerGenerator;
import com.peach.service.powerGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/power-generator")
public class powerGeneratorController {

    @Autowired
    private powerGeneratorService powerGeneratorService;

    private static int curr_pg_num = 1;

    public static void setCurr_pg_num(int curr_pg_num) {
        powerGeneratorController.curr_pg_num = curr_pg_num;
    }

    @GetMapping("/check-if-offthegrid")
    public boolean checkIfOffTheGrid(@CookieValue(value = "cookieEmail") String email) {
        return powerGeneratorService.checkIfOffTheGrid(email);
    }

    @PostMapping("/add")
    public String addPowerGenerator(@CookieValue(value = "cookieEmail") String email, @RequestBody Map<String, Object> queryMap) {
        Integer powerGeneratorNum = curr_pg_num;
        Integer monthlyKwh = (Integer) queryMap.get("monthlyKwh");
        Integer storageKwh = (Integer) queryMap.get("storageKwh");
        String generationType = (String) queryMap.get("generationType");
        powerGeneratorService.addPowerGenerator(new powerGenerator(email, powerGeneratorNum, monthlyKwh, storageKwh, generationType));
        curr_pg_num++;
        return "success";
    }

    @GetMapping("/view")
    public List<Map<String, Object>> viewPowerGenerator(@CookieValue(value = "cookieEmail") String email) {
        return powerGeneratorService.viewPowerGenerator(email);
    }

    @DeleteMapping("/delete/{powerGeneratorNum}")
    public String deletePowerGenerator(@CookieValue(value = "cookieEmail") String email, @PathVariable Integer powerGeneratorNum) {
        if(checkIfOffTheGrid(email) && powerGeneratorService.viewPowerGenerator(email).size()==1)
            return "not allowed";
        else {
            powerGeneratorService.deletePowerGenerator(email, powerGeneratorNum);
            return "success";
        }
    }
}