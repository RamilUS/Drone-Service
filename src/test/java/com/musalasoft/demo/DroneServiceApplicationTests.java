package com.musalasoft.demo;

import com.musalasoft.demo.repo.DroneRepo;
import com.musalasoft.demo.repo.MedicationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableWebMvc
@AutoConfigureMockMvc
class DroneServiceApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DroneRepo droneRepo;

    @Autowired
    MedicationRepo medicationRepo;

    @BeforeEach
    void before() {
        droneRepo.deleteAll();
        medicationRepo.deleteAll();
    }

    @Test
    void registerNewDrone_ReturnsSerialNumber() throws Exception {
        var serialNumber = "Drone_1";
        var type = "LIGHTWEIGHT";
        mockMvc.perform(post("/drone/1.0/api/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber", serialNumber)
                        .param("type", type)
                        .param("battery", "20"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(0)))
                .andExpect(jsonPath("$.errorCause", nullValue()))
                .andExpect(jsonPath("$.errorMessage", nullValue()))
                .andExpect(jsonPath("$.serialNumber", is(serialNumber)));
    }

    @Test
    void registerDroneWithTheSameId_returnErrorDescription() throws Exception {
        var serialNumber = "Drone_1";
        var type = "LIGHTWEIGHT";
        mockMvc.perform(post("/drone/1.0/api/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber", serialNumber)
                        .param("type", type)
                        .param("battery", "20"))
                .andDo(log())
                .andExpect(status().isOk());

        mockMvc.perform(post("/drone/1.0/api/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber", serialNumber)
                        .param("type", type)
                        .param("battery", "20"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(-1)))
                .andExpect(jsonPath("$.errorCause", is("com.musalasoft.demo.exception.RegistrationException")))
                .andExpect(jsonPath("$.errorMessage", is("Drone with this serial number is already registered")))
                .andExpect(jsonPath("$.serialNumber", nullValue()));
    }

    @Test
    void registerMedication_returnMedicationCode() throws Exception {
        var medicationCode = "MED_01";
        var name = "MED_01_nome";
        var weight = "100";
        var byteArray = "bytrarry".getBytes();
        mockMvc.perform(post("/drone/1.0/api/registerMed")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("medicationCode", medicationCode)
                        .param("name", name)
                        .param("weight", weight)
                        .content(byteArray))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(0)))
                .andExpect(jsonPath("$.errorCause", nullValue()))
                .andExpect(jsonPath("$.errorMessage", nullValue()))
                .andExpect(jsonPath("$.medicationCode", is(medicationCode)));
    }

    @Test
    void registerMedicationWithSameCode_returnErrorDescription() throws Exception {
        var medicationCode = "MED_01";
        var name = "MED_01_nome";
        var weight = "100";
        var byteArray = "bytrarry".getBytes();
        mockMvc.perform(post("/drone/1.0/api/registerMed")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("medicationCode", medicationCode)
                .param("name", name)
                .param("weight", weight)
                .content(byteArray));

        mockMvc.perform(post("/drone/1.0/api/registerMed")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("medicationCode", medicationCode)
                        .param("name", name)
                        .param("weight", weight)
                        .content(byteArray))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(-1)))
                .andExpect(jsonPath("$.errorCause", is("com.musalasoft.demo.exception.RegistrationException")))
                .andExpect(jsonPath("$.errorMessage", is("Medication with this code is already registered")));

    }

    @Test
    void registerMedicationWithNotValidCode_returnErrorDescription() throws Exception {
        var notValidMedicationCode = "MED_01v";
        var name = "MED_01_nome";
        var weight = "100";
        var byteArray = "bytrarry".getBytes();
        mockMvc.perform(post("/drone/1.0/api/registerMed")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("medicationCode", notValidMedicationCode)
                        .param("name", name)
                        .param("weight", weight)
                        .content(byteArray))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(-1)))
                .andExpect(jsonPath("$.errorCause", is("jakarta.validation.ConstraintViolationException")))
                .andExpect(jsonPath("$.errorMessage", is("registerMedication.medicationCode: allowed only upper case letters, underscore and numbers")));
    }

    @Test
    void registerMedicationWithNotValidName_returnErrorDescription() throws Exception {
        var medicationCode = "MED_01";
        var notValidName = "MED_01_nome&";
        var weight = "100";
        var byteArray = "bytrarry".getBytes();
        mockMvc.perform(post("/drone/1.0/api/registerMed")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("medicationCode", medicationCode)
                        .param("name", notValidName)
                        .param("weight", weight)
                        .content(byteArray))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(-1)))
                .andExpect(jsonPath("$.errorCause", is("jakarta.validation.ConstraintViolationException")))
                .andExpect(jsonPath("$.errorMessage", is("registerMedication.name: only letters, numbers, underscore and hyphen allowed")));
    }

    @Test
    void getAvailableDrones_returnListOfAvailableDrones() throws Exception {

        var serialNumber1 = "Drone_1";
        var serialNumber2 = "Drone_2";
        var serialNumber3 = "Drone_3";
        var type = "LIGHTWEIGHT";

        mockMvc.perform(post("/drone/1.0/api/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber", serialNumber1)
                        .param("type", type)
                        .param("battery", "20"))
                .andDo(log())
                .andExpect(status().isOk());

        mockMvc.perform(post("/drone/1.0/api/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber", serialNumber2)
                        .param("type", type)
                        .param("battery", "40"))
                .andDo(log())
                .andExpect(status().isOk());

        mockMvc.perform(post("/drone/1.0/api/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber", serialNumber3)
                        .param("type", type)
                        .param("battery", "60"))
                .andDo(log())
                .andExpect(status().isOk());

        mockMvc.perform(get("/drone/1.0/api/getAvailableDrones")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }
    @Test
    void loadDrone_returnMapOfLoadedMedsAndQuantity() throws Exception {

        var serialNumber = "Drone_1";
        var type = "LIGHTWEIGHT";
        var medicationCode1 = "MED_01";
        var medicationCode2 = "MED_02";
        var name = "MED_01_nome";
        var byteArray = "bytrarry".getBytes();
        mockMvc.perform(post("/drone/1.0/api/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber", serialNumber)
                        .param("type", type)
                        .param("battery", "50"))
                .andDo(log())
                .andExpect(status().isOk());

        mockMvc.perform(post("/drone/1.0/api/registerMed")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("medicationCode", medicationCode1)
                        .param("name", name)
                        .param("weight", "100")
                        .content(byteArray))
                .andDo(log());
        mockMvc.perform(post("/drone/1.0/api/registerMed")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("medicationCode", medicationCode2)
                        .param("name", name)
                        .param("weight", "300")
                        .content(byteArray))
                .andDo(log());

        mockMvc.perform(post("/drone/1.0/api/loadDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber",serialNumber)
                        .param("medicationCode",medicationCode1))
                .andDo(log());

        mockMvc.perform(post("/drone/1.0/api/loadDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber",serialNumber)
                        .param("medicationCode",medicationCode1))
                .andDo(log());

        mockMvc.perform(post("/drone/1.0/api/loadDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber",serialNumber)
                        .param("medicationCode",medicationCode2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(0)))
                .andExpect(jsonPath("$.errorCause", nullValue()))
                .andExpect(jsonPath("$.errorMessage", nullValue()))
                .andExpect(jsonPath("$.loadedMeds", is(Map.of(medicationCode2,1,medicationCode1,2))));
    }

    @Test
    void checkLoadedMedication_returnIsMedicationLoadedInDrone() throws Exception {

        var serialNumber = "Drone_1";
        var type = "LIGHTWEIGHT";
        var medicationCode1 = "MED_01";
        var medicationCode2 = "MED_02";
        var name = "MED_01_nome";
        var byteArray = "bytrarry".getBytes();
        mockMvc.perform(post("/drone/1.0/api/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber", serialNumber)
                        .param("type", type)
                        .param("battery", "50"))
                .andDo(log())
                .andExpect(status().isOk());

        mockMvc.perform(post("/drone/1.0/api/registerMed")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("medicationCode", medicationCode1)
                        .param("name", name)
                        .param("weight", "100")
                        .content(byteArray))
                .andDo(log());
        mockMvc.perform(post("/drone/1.0/api/registerMed")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("medicationCode", medicationCode2)
                        .param("name", name)
                        .param("weight", "300")
                        .content(byteArray))
                .andDo(log());

        mockMvc.perform(post("/drone/1.0/api/loadDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber",serialNumber)
                        .param("medicationCode",medicationCode1))
                .andDo(log());

        mockMvc.perform(post("/drone/1.0/api/loadDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber",serialNumber)
                        .param("medicationCode",medicationCode1))
                .andDo(log());

        mockMvc.perform(post("/drone/1.0/api/loadDrone")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber",serialNumber)
                        .param("medicationCode",medicationCode2))
                .andDo(log())
                .andExpect(status().isOk());

        mockMvc.perform(get("/drone/1.0/api/checkLoadedMedication")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("serialNumber",serialNumber)
                        .param("medicationCode",medicationCode2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));
    }
}
