package com; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepo;

    @Autowired
    private OPCUA opcService;

    // 1. LẤY DANH SÁCH
    @GetMapping
    public List<Recipe> getAll() {
        return recipeRepo.findAll();
    }

    // 2. LƯU
    @PostMapping
    public Recipe save(@RequestBody Recipe recipe) {
        return recipeRepo.save(recipe);
    }

    // 3. XÓA
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recipeRepo.deleteById(id);
    }

    // 4. LOAD TO MACHINE
    @PostMapping("/load/{id}")
    public String loadToMachine(@PathVariable Long id) {
        Optional<Recipe> match = recipeRepo.findById(id);
        if (match.isEmpty()) return "Error: Recipe not found";

        Recipe r = match.get();
        try {
            opcService.writeTag("Set Point", r.getSpeed().shortValue());
            opcService.writeTag("Momen HMI", r.getTorque().shortValue());
            opcService.writeTag("Start HMI", true);
            
            // dòng này để hiển thị lên dashboard
            MachineData.currentProductCode = r.getName();

            return "OK";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}