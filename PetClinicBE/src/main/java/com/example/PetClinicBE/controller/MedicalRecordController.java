package com.example.PetClinicBE.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.PetClinicBE.dto.request.CreateAppointmentRequest;
import com.example.PetClinicBE.dto.request.MedicalRecordDTO;
import com.example.PetClinicBE.entity.MedicalRecord;
import com.example.PetClinicBE.entity.User;
import com.example.PetClinicBE.service.MedicalRecordService;

import jakarta.validation.Valid;

@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    //Đặt lịch khám
    @PostMapping("/appointments")
    public ResponseEntity<MedicalRecord> createAppointment(
            @Validated @RequestBody CreateAppointmentRequest request) {
        MedicalRecord createdAppointment = medicalRecordService.createAppointment(request);
        return ResponseEntity.ok(createdAppointment);
    }
    
    //Sửa lịch khám
    @PutMapping("/appointments/{id}")
    public ResponseEntity<MedicalRecord> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody CreateAppointmentRequest request) {
        MedicalRecord updatedRecord = medicalRecordService.updateAppointment(id, request);
        return ResponseEntity.ok(updatedRecord);
    }
    
    //tạo hồ sơ
    @PostMapping("/medical_records")
    public ResponseEntity<MedicalRecord> createMedicalRecord(@Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord savedRecord = medicalRecordService.createMedicalRecord(medicalRecordDTO);
        return ResponseEntity.ok(savedRecord);
    }
    
    //sửa hồ sơ
    @PutMapping("/medical_records/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(id, medicalRecordDTO);
        return ResponseEntity.ok(updatedRecord);
    }
//    public ResponseEntity<MedicalRecord> createMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord,
//            @RequestParam String role) {
//        try {
//            MedicalRecord createdRecord = medicalRecordService.createMedicalRecord(medicalRecord, role);
//            return ResponseEntity.ok(createdRecord);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
    
    @GetMapping("/medical_records/{id}")
    public ResponseEntity<?> getMedicalRecordById(@PathVariable("id") Long id) {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(id);
        if (medicalRecord != null) {
            return ResponseEntity.ok(medicalRecord);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "MedicalRecord not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    @GetMapping("/medical_records")
    public ResponseEntity<Page<MedicalRecord>> getAllMedicalRecordsByPhoneOwner(
            @RequestParam String phoneOwner,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<MedicalRecord> records = medicalRecordService.getAllMedicalRecordsByPhoneOwner(PageRequest.of(page, size), phoneOwner);
        return ResponseEntity.ok(records);
    }

    @GetMapping("admin/noncompleted-medical-records")
    public ResponseEntity<Page<MedicalRecord>> getNoCompletedAllMedicalRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<MedicalRecord> records = medicalRecordService.getNonCompletedMedicalRecords(PageRequest.of(page, size));
        return ResponseEntity.ok(records);
    }

    @GetMapping("admin/completed-medical-records")
    public ResponseEntity<Page<MedicalRecord>> getCompletedMedicalRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<MedicalRecord> records = medicalRecordService.getCompletedMedicalRecords(PageRequest.of(page, size));
        return ResponseEntity.ok(records);
    }

    @GetMapping("admin/medical_records/search_completed")
    public ResponseEntity<Page<MedicalRecord>> searchCompletedMedicalRecords(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<MedicalRecord> records = medicalRecordService.searchCompletedRecords(keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("admin/medical_records/search_noncompleted")
    public ResponseEntity<Page<MedicalRecord>> searchNonCompletedMedicalRecords(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<MedicalRecord> records = medicalRecordService.searchNonCompletedRecords(keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/waiting-medical-records")
    public ResponseEntity<Page<MedicalRecord>> getWaitingMedicalRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<MedicalRecord> records = medicalRecordService.getWaitingMedicalRecords(PageRequest.of(page, size));
        return ResponseEntity.ok(records);
    }

    @PutMapping("/cancel_medical_records/{id}")
    public ResponseEntity<MedicalRecord> cancelMedicalRecord(@PathVariable Long id, @RequestBody Map<String, String> body) {
    	String role = body.get("role");
    	try {
            MedicalRecord canceledRecord = medicalRecordService.cancelMedicalRecord(id, role);
            return ResponseEntity.ok(canceledRecord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

//    @PutMapping("/medical_records/{id}")
//    public ResponseEntity<?> updateMedicalRecord(@PathVariable Long id,
//            @Valid @RequestBody MedicalRecord updatedMedicalRecord, @RequestParam String role) {
//        try {
//            MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(id, updatedMedicalRecord, role);
//            return ResponseEntity.ok(updatedRecord);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}