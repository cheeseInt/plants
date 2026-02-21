package ch.cheese.plants.service;

import ch.cheese.plants.config.Timeline;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.*;
import ch.cheese.plants.mapper.MeasurementMapper;
import ch.cheese.plants.mapper.PlantMapper;
import ch.cheese.plants.repository.BatteryLogRepository;
import ch.cheese.plants.repository.MeasurementRepository;
import ch.cheese.plants.repository.PlantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantImportServiceTest {

    @Mock private FytaService fytaService;
    @Mock private PlantMapper plantMapper;
    @Mock private MeasurementMapper measurementMapper;
    @Mock private PlantRepository plantRepository;
    @Mock private MeasurementRepository measurementRepository;
    @Mock private BatteryLogRepository batteryLogRepository;

    @InjectMocks
    private PlantImportService service;

    @Test
    void importPlants_whenApiReturnsNull_nothingIsSaved() {
        when(fytaService.fetchUserPlants()).thenReturn(null);

        service.importPlants();

        verify(plantRepository, never()).saveAll(any());
    }

    @Test
    void importPlants_whenApiReturnsNullPlantList_nothingIsSaved() {
        FytaUserPlantsResponse response = new FytaUserPlantsResponse();
        response.setPlants(null);
        when(fytaService.fetchUserPlants()).thenReturn(response);

        service.importPlants();

        verify(plantRepository, never()).saveAll(any());
    }

    @Test
    void importPlants_whenApiReturnsPlants_savesAll() {
        Plant plant = new Plant();
        plant.setId(1);
        FytaUserPlantsResponse response = new FytaUserPlantsResponse();
        response.setPlants(List.of(plant));
        when(fytaService.fetchUserPlants()).thenReturn(response);
        when(plantMapper.toEntity(any())).thenReturn(new PlantEntity());

        service.importPlants();

        verify(plantRepository).saveAll(any());
    }

    @Test
    void importPlantsFromFyta_whenApiReturnsNull_abortsBeforeDetails() {
        when(fytaService.fetchUserPlants()).thenReturn(null);

        service.importPlantsFromFyta(Timeline.DAY);

        verify(fytaService, never()).fetchUserPlantsDetail(any());
        verify(fytaService, never()).fetchMeasurements(any(), any());
    }

    @Test
    void importPlantsFromFyta_whenDetailIsNull_plantNotSavedAndBatteryNotLogged() {
        Plant plant = new Plant();
        plant.setId(1);
        FytaUserPlantsResponse response = new FytaUserPlantsResponse();
        response.setPlants(List.of(plant));
        when(fytaService.fetchUserPlants()).thenReturn(response);
        when(plantMapper.toEntity(any())).thenReturn(new PlantEntity());
        when(fytaService.fetchUserPlantsDetail("1")).thenReturn(null);
        when(fytaService.fetchMeasurements(any(), any())).thenReturn(null);

        service.importPlantsFromFyta(Timeline.DAY);

        verify(plantRepository, never()).save(any());
        verify(batteryLogRepository, never()).save(any());
    }

    @Test
    void importPlantsFromFyta_whenMeasurementWrapperIsNull_noMeasurementsSaved() {
        Plant plant = new Plant();
        plant.setId(1);
        FytaUserPlantsResponse response = new FytaUserPlantsResponse();
        response.setPlants(List.of(plant));
        when(fytaService.fetchUserPlants()).thenReturn(response);
        when(plantMapper.toEntity(any())).thenReturn(new PlantEntity());
        when(fytaService.fetchUserPlantsDetail("1")).thenReturn(detailWithNoSensors());
        when(fytaService.fetchMeasurements("1", "day")).thenReturn(null);

        service.importPlantsFromFyta(Timeline.DAY);

        verify(measurementRepository, never()).save(any());
    }

    private FytaPlantDetailResponse detailWithNoSensors() {
        FytaPlantDetailResponse detail = new FytaPlantDetailResponse();
        FytaPlantDetailResponse.Plant plant = new FytaPlantDetailResponse.Plant();
        plant.setSensors(List.of());
        detail.setPlant(plant);
        return detail;
    }
}