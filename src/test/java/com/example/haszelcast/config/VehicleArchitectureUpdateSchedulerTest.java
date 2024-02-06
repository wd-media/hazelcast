package com.example.haszelcast.config;

import com.example.haszelcast.service.VehicleArchitectureService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.CPSubsystem;
import com.hazelcast.cp.lock.FencedLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleArchitectureUpdateSchedulerTest {

    @Mock
    private HazelcastInstance hazelcastInstance;

    @Mock
    private CPSubsystem cpSubsystem;

    @Mock
    private VehicleArchitectureService vehicleArchitectureService;

    @Mock
    private FencedLock fencedLock;

    @InjectMocks
    private VehicleArchitectureUpdateScheduler scheduler;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        when(this.hazelcastInstance.getCPSubsystem()).thenReturn(this.cpSubsystem);
        when(this.cpSubsystem.getLock(anyString())).thenReturn(this.fencedLock);
    }


    @Test
    void testExecuteTaskWhenLockNotAcquired() {
        // Arrange
        when(this.fencedLock.tryLock()).thenReturn(false);

        // Act
        this.scheduler.executeVehicleArchitectureUpdateTask(false);

        // Assert
        verify(this.fencedLock, times(1)).tryLock();
    }


}
