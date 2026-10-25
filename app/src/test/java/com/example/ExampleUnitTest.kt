package com.example

import com.example.model.AetherAppType
import com.example.viewmodel.AetherSuiteViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testAppNavigationAndHistory() {
        val vm = AetherSuiteViewModel()
        assertEquals(AetherAppType.HOME, vm.activeApp.value)

        vm.openApp(AetherAppType.MAIL)
        assertEquals(AetherAppType.MAIL, vm.activeApp.value)

        vm.openApp(AetherAppType.DRIVE)
        assertEquals(AetherAppType.DRIVE, vm.activeApp.value)

        val handled = vm.handleBack()
        assertTrue(handled)
        assertEquals(AetherAppType.MAIL, vm.activeApp.value)
    }

    @Test
    fun testEmailSending() {
        val vm = AetherSuiteViewModel()
        val initialCount = vm.emails.value.size
        vm.sendEmail("test@aether.cloud", "Test Subject", "Meeting notes preview")
        assertEquals(initialCount + 1, vm.emails.value.size)
        assertEquals("Test Subject", vm.emails.value.first().subject)
    }

    @Test
    fun testTaskCompletion() {
        val vm = AetherSuiteViewModel()
        val firstTask = vm.omniTasks.value.first()
        val initialStatus = firstTask.isCompleted
        vm.toggleTask(firstTask.id)
        assertEquals(!initialStatus, vm.omniTasks.value.first().isCompleted)
    }
}
