package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;

import com.pinyougou.service.SellerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference(timeout = 10000)
    private SellerService sellerService;



    @PostMapping("/save")
    public boolean save(@RequestBody Seller seller) {
        try {

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(password);
            sellerService.save(seller);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/findSellerBySellerId")
    public Seller findSellerBySellerId(HttpServletRequest request){
        try {
            String sellerId = request.getRemoteUser();
            Seller seller =  sellerService.findSellerBySellerId(sellerId);
            return seller;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @PostMapping("/updateSeller")
    public boolean updateSeller(@RequestBody Seller seller){
        try {
            sellerService.updateSeller(seller);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @PostMapping("/updatePasswrod")
    public boolean updatePasswrod(HttpServletRequest request,@RequestBody Map<String ,String> map){
        try {
            Seller seller = findSellerBySellerId(request);
            String passwordOld = seller.getPassword();
            String sellerId = seller.getSellerId();
            String password = map.get("pw");
            String pw1 = map.get("pw1");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encode2 = passwordEncoder.encode(pw1);
            if (passwordEncoder.matches(password, passwordOld)){
               sellerService.updatePasswrod(sellerId,encode2);
               return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
