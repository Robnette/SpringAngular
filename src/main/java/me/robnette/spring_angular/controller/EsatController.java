package me.robnette.spring_angular.controller;

import me.robnette.spring_angular.dao.Esat;
import me.robnette.spring_angular.dao.EsatContact;
import me.robnette.spring_angular.model.RestResponse;
import me.robnette.spring_angular.repository.EsatContactRepository;
import me.robnette.spring_angular.repository.EsatRepository;
import me.robnette.spring_angular.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class EsatController {
    @Autowired
    private EsatRepository esatRepository;
    @Autowired
    private EsatContactRepository esatContactRepository;

    @RequestMapping(value = "/esats", method = RequestMethod.GET)
    public List<Esat> users() {
        return esatRepository.findAll();
    }

    @RequestMapping(value = "/contacts/{codeEsat}", method = RequestMethod.GET)
    public List<EsatContact> contacts(@PathVariable String codeEsat) {
        return esatContactRepository.findByIdEsat(esatRepository.findOneByCodeEsat(codeEsat).getIdEsat());
    }

    @RequestMapping(value = "/contact/{codeEsat}", method = RequestMethod.POST)
    public EsatContact createContact(@PathVariable String codeEsat, @RequestBody EsatContact pojo) {
        Long idEsat = esatRepository.findOneByCodeEsat(codeEsat).getIdEsat();

        pojo.setIdEsat(idEsat);
        esatContactRepository.save(pojo);

        return pojo;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.PUT)
    public EsatContact updateContact(@RequestBody EsatContact pojo) {

        EsatContact esatContact = esatContactRepository.findOneByIdEsatContact(pojo.getIdEsatContact());
        esatContact.setEmail(pojo.getEmail());
        esatContact.setMobile(pojo.getMobile());
        esatContact.setNom(pojo.getNom());
        esatContact.setPassword(pojo.getPassword());
        esatContact.setPrenom(pojo.getPrenom());
        esatContact.setTelephone(pojo.getTelephone());

        esatContactRepository.save(esatContact);
        return esatContact;
    }

    @RequestMapping(value = "/contact/{id}", method = RequestMethod.DELETE)
    public RestResponse deleteContact(@PathVariable Long id) {
        esatContactRepository.delete(id);
        return new RestResponse("ok");
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile uploadfile) {
        if (uploadfile.isEmpty()) {
            return new ResponseEntity(new RestResponse("please select a file!"), HttpStatus.OK);
        }

        try {
            Util.saveUploadedFiles(Arrays.asList(uploadfile));
        } catch (IOException e) {
            return new ResponseEntity(new RestResponse("error!"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new RestResponse("Successfully uploaded - " +
            uploadfile.getOriginalFilename()), new HttpHeaders(), HttpStatus.OK);
    }

}
