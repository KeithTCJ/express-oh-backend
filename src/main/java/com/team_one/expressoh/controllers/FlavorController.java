package com.team_one.expressoh.controllers;

import com.team_one.expressoh.exception.ResourceNotFoundException;
import com.team_one.expressoh.model.Flavor;
import com.team_one.expressoh.model.Product;
import com.team_one.expressoh.repository.FlavorRepository;
import com.team_one.expressoh.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/api/flavor")
@CrossOrigin("*")
public class FlavorController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FlavorRepository flavorRepository;

    // Get all flavors (optionally filter by name)
    @GetMapping("")
    public ResponseEntity<Object> getAllFlavors(@RequestParam(required = false) String name) {
        List<Flavor> result = new ArrayList<>();

        if (name == null) {
            result.addAll(flavorRepository.findAll());
        } else {
            result.addAll(flavorRepository.findByNameContaining(name));
        }

        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // Get a single flavor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getFlavor(@PathVariable("id") Integer id) {
        Flavor result = flavorRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // Get all flavors of a product by product ID
    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getFlavorsByProductId(@PathVariable("id") Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }

        List<Flavor> result = flavorRepository.findFlavorsByProductsId(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // Get all products by flavor ID
    @GetMapping("/{id}/product")
    public ResponseEntity<Object> getProductsByFlavorId(@PathVariable("id") Integer id) {
        if (!flavorRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }

        List<Product> result = productRepository.findProductsByFlavorsId(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // Save new flavors
    @PostMapping("/save")
    public ResponseEntity<Object> saveFlavors(@RequestBody List<Flavor> flavors) {
        List<Flavor> result = flavorRepository.saveAll(flavors);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Save or update flavor(s) to a product
    @RequestMapping(value = {"/save/product/{id}", "/update/product/{id}"}, method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Object> saveFlavorsByProductId(@PathVariable("id") Integer id, @RequestBody List<Flavor> flavors) {
        List<Flavor> newFlavorList = new ArrayList<>();

        for (Flavor flavor : flavors) {
            Flavor newFlavor = flavorRepository.findById(flavor.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Flavor id " + flavor.getId() + " is not found."));
            newFlavorList.add(newFlavor);
        }

        Product product = productRepository.findById(id).map(_product -> {
            List<Flavor> currentList = new ArrayList<>(_product.getFlavors());

            for (Flavor newFlavor : newFlavorList) {
                if (!currentList.contains(newFlavor)) {
                    _product.getFlavors().add(newFlavor);
                }
            }

            for (Flavor currentFlavor : currentList) {
                if (!newFlavorList.contains(currentFlavor)) {
                    _product.getFlavors().remove(currentFlavor);
                }
            }

            return productRepository.save(_product);
        }).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // Delete single flavor
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFlavor(@PathVariable("id") Integer id) {
        Flavor result = flavorRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        flavorRepository.deleteById(id);

        if (flavorRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Delete multiple flavors
    @DeleteMapping("")
    public ResponseEntity<Object> deleteFlavors(@RequestBody List<Flavor> flavors) {
        List<Flavor> deleteList = new ArrayList<>();

        for (Flavor flavor : flavors) {
            Flavor f = flavorRepository.findById(flavor.getId()).orElseThrow(ResourceNotFoundException::new);
            deleteList.add(f);
        }

        for (Flavor flavor : deleteList) {
            flavorRepository.deleteById(flavor.getId());
        }

        return new ResponseEntity<>(deleteList, HttpStatus.OK);
    }

    // Remove flavor(s) from a product
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> deleteFlavorsByProductId(@PathVariable("id") Integer id, @RequestBody List<Flavor> flavors) {
        List<Flavor> removeList = new ArrayList<>();

        for (Flavor flavor : flavors) {
            Flavor f = flavorRepository.findById(flavor.getId()).orElseThrow(ResourceNotFoundException::new);
            removeList.add(f);
        }

        Product result = productRepository.findById(id).map(_product -> {
            for (Flavor flavor : removeList) {
                _product.removeFlavor(flavor.getId());
            }
            return productRepository.save(_product);
        }).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
