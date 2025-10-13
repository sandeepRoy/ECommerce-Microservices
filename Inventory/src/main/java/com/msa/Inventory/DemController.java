package com.msa.Inventory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class DemController {

    @GetMapping("/demo")
    public ResponseEntity<String> getDemo() {
        return new ResponseEntity<>("Demo", HttpStatus.OK);
    }
}
