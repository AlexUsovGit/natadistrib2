package com.example.sweater.controller;


import com.example.sweater.domain.NewLabel;
import com.example.sweater.domain.Product;
import com.example.sweater.domain.basedictionary.Composition;
import com.example.sweater.domain.basedictionary.ProductName;
import com.example.sweater.repos.ProductRepo;
import com.example.sweater.repos.basedictionaryrepos.CompositionRepo;
import com.example.sweater.repos.basedictionaryrepos.ProductNameRepo;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Controller
public class ProductController {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CompositionRepo compositionRepo;
    @Autowired
    private ProductNameRepo productNameRepo;



    @GetMapping("/producttable")
    public String producttable(Map<String, Object> model) {
        Iterable<Product> products = productRepo.findAllByOrderByIdDesc();
        model.put("products",products);

       /* Iterable<Composition> compositions = compositionRepo.findAll();
        model.put("compositions",compositions);

        Iterable<ProductName> productNames = productNameRepo.findAll();
        model.put("productNames",productNames);*/
        return "producttable";
    }


    @GetMapping("/productadd")
    public String productadd(Map<String, Object> model) {
        Iterable<Product> products = productRepo.findAllByOrderByIdDesc();
        model.put("products",products);

        Iterable<Composition> compositions = compositionRepo.findAll();
        model.put("compositions",compositions);

        Iterable<ProductName> productNames = productNameRepo.findAll();
        model.put("productNames",productNames);
        return "productadd";
    }

    @PostMapping("/producttable")
    public String add(String productName, String gender, String size, String trademark,
                      String importer, String manufacturer, String article,
                      String code, String composition, String season, String barcode, String note,
                      String quantity,  String dateArrive, String importPrice,String coefficient,
                      String retailPrice, String countryOfEntry,String currency, String course, Integer isDistrib,
                      Map<String, Object> model) throws ParseException {

        Product product = new Product(productName,gender,size,trademark,importer,
                manufacturer,article, code, composition, season,barcode,note,
                quantity,   dateArrive,  importPrice,coefficient,retailPrice, countryOfEntry,currency, course,isDistrib);

        product.setIsDistrib(0);
        productRepo.save(product);
        product.setBarcode(String.format("%013d", product.getId()));
        productRepo.save(product);



        Iterable<Product> products = productRepo.findAllByOrderByIdDesc();
        model.put("products",products);

        Iterable<Composition> compositions = compositionRepo.findAll();
        model.put("compositions",compositions);

        Iterable<ProductName> productNames = productNameRepo.findAll();
        model.put("productNames",productNames);

        return "producttable";
    }

    @PostMapping("/searchdoc")
    public String filter (@RequestParam String filter, Map<String, Object> model) throws IOException {
        Iterable<Product> products;

         if(filter!=null && !filter.isEmpty()){
            products = productRepo.findByBarcodeOrderByIdAsc(filter);

             for (Product product : products) {


             NewLabel documentPdf = new NewLabel(product);
             try {
                 documentPdf.createPdf();
             } catch (DocumentException e) {
                 e.printStackTrace();
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
             }



         }else {
            products = productRepo.findAllByOrderByIdDesc();


        }

        model.put("products", products);

         Iterable<Composition> compositions = compositionRepo.findAll();
        model.put("compositions",compositions);


        Iterable<ProductName> productNames = productNameRepo.findAll();
        model.put("productNames",productNames);

        return "documentpdf";
    }

    @PostMapping("/addToDistrib")
    public String setDistrib (@RequestParam String id, Map<String, Object> model) throws IOException {
        Iterable<Product> products;


        if(id!=null && !id.isEmpty()){
            Long longId = Long.parseLong(id);
            products = productRepo.findByIdOrderByIdAsc(longId);

            for (Product product : products) {
                product.setIsDistrib(1);

                productRepo.save(product);

            }
        }
        products = productRepo.findAllByOrderByIdDesc();

        model.put("products", products);

        Iterable<Composition> compositions = compositionRepo.findAll();
        model.put("compositions",compositions);
        Iterable<ProductName> productNames = productNameRepo.findAll();
        model.put("productNames",productNames);



        return "producttable";
    }
    @PostMapping("/deleteFromDistrib")
    public String delDistrib (@RequestParam String id, Map<String, Object> model) throws IOException {
        Iterable<Product> products;


        if(id!=null && !id.isEmpty()){
            Long longId = Long.parseLong(id);
            products = productRepo.findByIdOrderByIdAsc(longId);

            for (Product product : products) {
                product.setIsDistrib(0);

                productRepo.save(product);

            }
        }
        products = productRepo.findAllByOrderByIdDesc();

        model.put("products", products);

        Iterable<Composition> compositions = compositionRepo.findAll();
        model.put("compositions",compositions);
        Iterable<ProductName> productNames = productNameRepo.findAll();
        model.put("productNames",productNames);



        return "producttable";
    }

    @PostMapping("/documentDistribution")
    public String producttableDistributio(@RequestParam String id, Map<String, Object> model) {
        Integer intId = Integer.parseInt(id);
        Iterable<Product> products = productRepo.findByisDistrib(intId);
        model.put("products",products);

        return "documentDistribution";
    }

}
