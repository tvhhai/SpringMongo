package com.app.springmongo.controller;

import com.app.springmongo.dto.request.ProductRequest;
import com.app.springmongo.entity.Product;
import com.app.springmongo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ProductController {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAll() {
        try {
            List<Product> tableConfigs = new ArrayList<>(productService.getAll());
            return new ResponseEntity<>(tableConfigs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{typeId}")
    public ResponseEntity<Product> getById(@PathVariable("typeId") String typeId) {
        try {
            Optional<Product> optional = productService.getById(typeId);
            return optional.map(Product -> new ResponseEntity<>(Product, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(path = "/product", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<Product> save(@RequestPart("user") ProductRequest productRequest, @RequestPart("images") MultipartFile[] files) {
        try {
            Path staticPath = Paths.get("src\\main\\resources\\static\\");
            Path imagePath = Paths.get("images");

            if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
                Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
            }

            List<String> fileNames = new ArrayList<>();

            // read and write the file to the local folder
            Arrays.stream(files).forEach(file -> {
                byte[] bytes = new byte[0];
                try {
                    bytes = file.getBytes();
                    fileNames.add(file.getOriginalFilename());
                    Path images = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(file.getOriginalFilename());

                    try (OutputStream os = Files.newOutputStream(images)) {
                        os.write(bytes);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            });

            Product product = new Product();
            product.setImage(fileNames);
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setImportPrice(productRequest.getImportPrice());
            product.setProductType(productRequest.getProductType());
            product.setSale(productRequest.getSale());

            return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
        } catch (
                Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/product/{typeId}")
    public ResponseEntity<Product> update(@PathVariable("typeId") String typeId, @RequestBody Product product) {
        try {
            Optional<Product> productOptional = productService.getById(typeId);
            if (productOptional.isPresent()) {
                Product _proProduct = productOptional.get();
                _proProduct.setName(product.getName());
                return new ResponseEntity<>(productService.save(_proProduct), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/product/{typeId}")
    public ResponseEntity<Product> delete(@PathVariable("typeId") List<Product> typeId) {
        try {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
