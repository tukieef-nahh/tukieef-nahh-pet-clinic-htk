package com.example.PetClinicBE.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.PetClinicBE.dto.request.CreateAppointmentRequest;
import com.example.PetClinicBE.dto.request.MedicalRecordDTO;
import com.example.PetClinicBE.entity.MedicalRecord;
import com.example.PetClinicBE.repository.MedicalRecordRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

	@Autowired
    private final MedicalRecordRepository medicalRecordRepository;

	//tạo hồ sơ
	public MedicalRecord createMedicalRecord(MedicalRecordDTO dto) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setSequenceNumber(dto.getSequenceNumber());
        medicalRecord.setAppointmentTime(dto.getAppointmentTime());
        medicalRecord.setNameOwner(dto.getNameOwner());
        medicalRecord.setPhoneOwner(dto.getPhoneOwner());
        medicalRecord.setNameVeterinarian(dto.getNameVeterinarian());
        medicalRecord.setPhoneVeterinarian(dto.getPhoneVeterinarian());
        medicalRecord.setNamePet(dto.getNamePet());
        medicalRecord.setAge(dto.getAge());
        medicalRecord.setSpecies(dto.getSpecies());
        medicalRecord.setBreed(dto.getBreed());
        medicalRecord.setDescription(dto.getDescription());
        medicalRecord.setDiagnosis(dto.getDiagnosis());
        medicalRecord.setTreatment(dto.getTreatment());
        medicalRecord.setStatus(MedicalRecord.AppointmentStatus.valueOf(dto.getStatus().toUpperCase()));
        medicalRecord.setCreatedAt(LocalDateTime.now());
        medicalRecord.setUpdatedAt(LocalDateTime.now());

        return medicalRecordRepository.save(medicalRecord);
    }
	
	//sửa hồ sơ
	public MedicalRecord updateMedicalRecord(Long id, MedicalRecordDTO dto) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(id);
        if (optionalMedicalRecord.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy hồ sơ khám với ID: " + id);
        }

        MedicalRecord medicalRecord = optionalMedicalRecord.get();

        if (medicalRecord.getStatus() != MedicalRecord.AppointmentStatus.WAITING) {
            throw new IllegalStateException("Chỉ có thể chỉnh sửa lịch khám khi trạng thái là WAITING.");
        }

        if (dto.getSequenceNumber() != null) medicalRecord.setSequenceNumber(dto.getSequenceNumber());
        if (dto.getAppointmentTime() != null) medicalRecord.setAppointmentTime(dto.getAppointmentTime());
        if (dto.getNameOwner() != null) medicalRecord.setNameOwner(dto.getNameOwner());
        if (dto.getPhoneOwner() != null) medicalRecord.setPhoneOwner(dto.getPhoneOwner());
        if (dto.getNameVeterinarian() != null) medicalRecord.setNameVeterinarian(dto.getNameVeterinarian());
        if (dto.getPhoneVeterinarian() != null) medicalRecord.setPhoneVeterinarian(dto.getPhoneVeterinarian());
        if (dto.getNamePet() != null) medicalRecord.setNamePet(dto.getNamePet());
        if (dto.getAge() != null) medicalRecord.setAge(dto.getAge());
        if (dto.getSpecies() != null) medicalRecord.setSpecies(dto.getSpecies());
        if (dto.getBreed() != null) medicalRecord.setBreed(dto.getBreed());
        if (dto.getDescription() != null) medicalRecord.setDescription(dto.getDescription());
        if (dto.getDiagnosis() != null) medicalRecord.setDiagnosis(dto.getDiagnosis());
        if (dto.getTreatment() != null) medicalRecord.setTreatment(dto.getTreatment());
        medicalRecord.setStatus(MedicalRecord.AppointmentStatus.valueOf(dto.getStatus().toUpperCase()));

        medicalRecord.setUpdatedAt(java.time.LocalDateTime.now());
        return medicalRecordRepository.save(medicalRecord);
    }
//    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord, String role) {
//        if (role.equals("USER")) {
//            // Kiểm tra số lượng hồ sơ trong ngày
//            long count = medicalRecordRepository.countByAppointmentTimeAndStatusNotIn(
//                    medicalRecord.getAppointmentTime().toLocalDate(),
//                    List.of(MedicalRecord.AppointmentStatus.CANCELED_BY_USER,
//                            MedicalRecord.AppointmentStatus.CANCELED_BY_ADMIN));
//            if (count >= 5) {
//                throw new IllegalArgumentException(
//                        "Đã hết thời gian đăng ký lịch khám trong ngày, vui lòng chọn ngày khác.");
//            } else {
//                // Tạo số thứ tự khám
//                Optional<Integer> maxSequence = medicalRecordRepository
//                        .findMaxSequenceNumberByDate(medicalRecord.getAppointmentTime().toLocalDate());
//                int sequenceNumber = maxSequence.orElse(0) + 1;
//                medicalRecord.setSequenceNumber(sequenceNumber);
//                medicalRecord.setStatus(MedicalRecord.AppointmentStatus.WAITING);
//                medicalRecord.setCreatedAt(medicalRecord.getCreatedAt() != null ? medicalRecord.getCreatedAt()
//                        : java.time.LocalDateTime.now());
//            }
//        } else if (role.equals("VETERINARIAN")) {
//            medicalRecord.setStatus(MedicalRecord.AppointmentStatus.COMPLETED);
//        }
//
//        return medicalRecordRepository.save(medicalRecord);
//    }
	
	//tạo lịch khám
	public MedicalRecord createAppointment(CreateAppointmentRequest request) {
//		long count = medicalRecordRepository.countByAppointmentTimeAndStatusNotIn(
//				request.getAppointmentTime().toLocalDate(),
//              List.of(MedicalRecord.AppointmentStatus.CANCELED_BY_USER,
//                      MedicalRecord.AppointmentStatus.CANCELED_BY_ADMIN));
//      if (count >= 10) {
//          throw new IllegalArgumentException(
//                  "Đã hết thời gian đăng ký lịch khám trong ngày, vui lòng chọn ngày khác.");
//      } else {
//          // Tạo số thứ tự khám
//          Optional<Integer> maxSequence = medicalRecordRepository
//                  .findMaxSequenceNumberByDate(request.getAppointmentTime().toLocalDate());
//          int sequenceNumber = maxSequence.orElse(0) + 1;
//		medicalRecord.setSequenceNumber(sequenceNumber);
		
		MedicalRecord medicalRecord = new MedicalRecord();
		
        medicalRecord.setAppointmentTime(request.getAppointmentTime());
        medicalRecord.setNameOwner(request.getNameOwner());
        medicalRecord.setPhoneOwner(request.getPhoneOwner());
        medicalRecord.setNamePet(request.getNamePet());
        medicalRecord.setAge(request.getAge());
        medicalRecord.setSpecies(request.getSpecies());
        medicalRecord.setBreed(request.getBreed());
        medicalRecord.setDescription(request.getDescription());
        medicalRecord.setStatus(MedicalRecord.AppointmentStatus.WAITING);

        return medicalRecordRepository.save(medicalRecord);
//      }
    }
	
	//sửa lịch khám
	public MedicalRecord updateAppointment(Long id, CreateAppointmentRequest request) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(id);
        if (optionalMedicalRecord.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy hồ sơ khám với ID: " + id);
        }

        MedicalRecord medicalRecord = optionalMedicalRecord.get();

        if (medicalRecord.getStatus() != MedicalRecord.AppointmentStatus.WAITING) {
            throw new IllegalStateException("Chỉ có thể chỉnh sửa lịch khám khi trạng thái là WAITING.");
        }

        if (request.getAppointmentTime() != null) medicalRecord.setAppointmentTime(request.getAppointmentTime());
        if (request.getNameOwner() != null) medicalRecord.setNameOwner(request.getNameOwner());
        if (request.getPhoneOwner() != null) medicalRecord.setPhoneOwner(request.getPhoneOwner());
        if (request.getNamePet() != null) medicalRecord.setNamePet(request.getNamePet());
        if (request.getAge() != null) medicalRecord.setAge(request.getAge());
        if (request.getSpecies() != null) medicalRecord.setSpecies(request.getSpecies());
        if (request.getBreed() != null) medicalRecord.setBreed(request.getBreed());
        if (request.getDescription() != null) medicalRecord.setDescription(request.getDescription());

        medicalRecord.setUpdatedAt(java.time.LocalDateTime.now());
        return medicalRecordRepository.save(medicalRecord);
    }
    
    public MedicalRecord getMedicalRecord(Long id) {
		Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
		return medicalRecord.orElse(null);
	}

    public Page<MedicalRecord> getAllMedicalRecordsByPhoneOwner(Pageable pageable, String phoneOwner) {
        return medicalRecordRepository.findAllByPhoneOwner(pageable, phoneOwner);
    }

    public Page<MedicalRecord> getNonCompletedMedicalRecords(Pageable pageable) {
        return medicalRecordRepository.findByStatusNot(MedicalRecord.AppointmentStatus.COMPLETED, pageable);
    }

    public Page<MedicalRecord> getCompletedMedicalRecords(Pageable pageable) {
        return medicalRecordRepository.findByStatus(MedicalRecord.AppointmentStatus.COMPLETED, pageable);
    }
    
    public Page<MedicalRecord> getWaitingMedicalRecords(Pageable pageable) {
        return medicalRecordRepository.findByStatusWaiting(pageable);
    }

    public MedicalRecord cancelMedicalRecord(Long id, String role) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (!medicalRecord.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy hồ sơ với ID " + id);
        }
        MedicalRecord record = medicalRecord.get();

        if (role.equals("USER")) {
            record.setStatus(MedicalRecord.AppointmentStatus.CANCELED_BY_USER);
        } else if (role.equals("ADMIN")) {
            record.setStatus(MedicalRecord.AppointmentStatus.CANCELED_BY_ADMIN);
        }

        return medicalRecordRepository.save(record);
    }

//    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord updatedMedicalRecord, String role) {
//        Optional<MedicalRecord> existingRecordOpt = medicalRecordRepository.findById(id);
//        if (!existingRecordOpt.isPresent()) {
//            throw new IllegalArgumentException("Không tìm thấy hồ sơ với ID " + id);
//        }
//        MedicalRecord existingRecord = existingRecordOpt.get();
//
//        if (existingRecord.getStatus() == MedicalRecord.AppointmentStatus.CANCELED_BY_USER ||
//            existingRecord.getStatus() == MedicalRecord.AppointmentStatus.CANCELED_BY_ADMIN ||
//            existingRecord.getStatus() == MedicalRecord.AppointmentStatus.COMPLETED) {
//            throw new IllegalArgumentException("Không thể cập nhật hồ sơ trong trạng thái này.");
//        }
//
//        if (role.equals("USER")) {
//            existingRecord.setAppointmentTime(updatedMedicalRecord.getAppointmentTime());
//            existingRecord.setNamePet(updatedMedicalRecord.getNamePet());
//            existingRecord.setSpecies(updatedMedicalRecord.getSpecies());
//            existingRecord.setBreed(updatedMedicalRecord.getBreed());
//            existingRecord.setDescription(updatedMedicalRecord.getDescription());
//            existingRecord.setNameOwner(updatedMedicalRecord.getNameOwner());
//            existingRecord.setPhoneOwner(updatedMedicalRecord.getPhoneOwner());
//            existingRecord.setAge(updatedMedicalRecord.getAge());
//            existingRecord.setStatus(MedicalRecord.AppointmentStatus.WAITING);
//        } else if (role.equals("VETERINARIAN")) {
//            existingRecord.setAppointmentTime(updatedMedicalRecord.getAppointmentTime());
//            existingRecord.setNamePet(updatedMedicalRecord.getNamePet());
//            existingRecord.setSpecies(updatedMedicalRecord.getSpecies());
//            existingRecord.setBreed(updatedMedicalRecord.getBreed());
//            existingRecord.setDescription(updatedMedicalRecord.getDescription());
//            existingRecord.setNameOwner(updatedMedicalRecord.getNameOwner());
//            existingRecord.setPhoneOwner(updatedMedicalRecord.getPhoneOwner());
//            existingRecord.setNameVeterinarian(updatedMedicalRecord.getNameVeterinarian());
//            existingRecord.setPhoneVeterinarian(updatedMedicalRecord.getPhoneVeterinarian());
//            existingRecord.setAge(updatedMedicalRecord.getAge());
//            existingRecord.setDiagnosis(updatedMedicalRecord.getDiagnosis());
//            existingRecord.setTreatment(updatedMedicalRecord.getTreatment());
//            existingRecord.setStatus(MedicalRecord.AppointmentStatus.COMPLETED);
//            
//        } else if (role.equals("ADMIN")) {
//            updatedMedicalRecord.setStatus(existingRecord.getStatus());
//        }
//        return medicalRecordRepository.save(existingRecord);
//    }

    public Page<MedicalRecord> searchCompletedRecords(String keyword, Pageable pageable) {
        return medicalRecordRepository.searchCompletedRecords(keyword, pageable);
    }

    public Page<MedicalRecord> searchNonCompletedRecords(String keyword, Pageable pageable) {
        return medicalRecordRepository.searchNonCompletedRecords(keyword, pageable);
    }
}