package com.tyshko.data.repository

import com.tyshko.data.local.ToDoDao
import com.tyshko.data.local.entity.ToDoEntity
import com.tyshko.data.network.NetworkApi
import com.tyshko.domain.model.ToDoModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToDoRepositoryImplTest {

    private lateinit var toDoDao: ToDoDao
    private lateinit var networkApi: NetworkApi
    private lateinit var toDoRepository: ToDoRepositoryImpl

    @Before
    fun setUp(){
        toDoDao = mockk(relaxUnitFun = true)
        networkApi = mockk(relaxUnitFun = true)
        toDoRepository = ToDoRepositoryImpl(toDoDao, networkApi)
    }

    @Test
    fun getToDos() = runTest {

        val entity = listOf(ToDoEntity(1, "Test title", "Test description", false))
        every { toDoDao.getToDos() } returns flowOf(entity)

        val result = toDoRepository.getToDos()

        val models = result.first()
        assertEquals(1, models.size)
        assertEquals("Test title", models[0].title)
        assertEquals("Test description", models[0].description)
        assertEquals(false, models[0].isCompleted)
    }

    @Test
    fun insertToDo() = runTest {
        val model = ToDoModel(2, "Test title", "Test description", false)
        coEvery { toDoDao.insertToDO(any()) } returns 1

        val result = toDoRepository.insertToDo(model)

        assertTrue(result)
        coVerify {
            toDoDao.insertToDO(match {
                it.id == model.id &&
                it.title == model.title &&
                it.description == model.description &&
                it.isCompleted == model.isCompleted
            })
        }
    }

    @Test
    fun getCertainToDo() = runTest {
        val model = listOf(
            ToDoEntity(2, "Test title 2", "Test description 2", false),
            ToDoEntity(3, "Test title 3", "Test description 3", true),
            ToDoEntity(4, "Test title 4", "Test description 4", false),
        )
        coEvery { toDoDao.getCertainToDO(3L) } returns model[1]

        val result = toDoRepository.getCertainToDo(3L)

        assertEquals(model[1].id, result?.id)
        assertEquals(model[1].title, result?.title)
        assertEquals(model[1].description, result?.description)
        assertEquals(model[1].isCompleted, result?.isCompleted)
    }

    @Test
    fun getCertainToDo_returnsNullIfNotFound() = runTest {
        coEvery { toDoDao.getCertainToDO(999L) } returns null

        val result = toDoRepository.getCertainToDo(999L)

        assertNull(result)
    }

    @Test
    fun updateToDo_success() = runTest {
        val model = ToDoModel(4, "Updated title", "Updated desc", true)
        coEvery { toDoDao.updateToDo(any()) } returns 1

        val result = toDoRepository.updateToDo(model)

        assertTrue(result)
        coVerify {
            toDoDao.updateToDo(match {
                it.id == model.id &&
                        it.title == model.title &&
                        it.description == model.description &&
                        it.isCompleted == model.isCompleted
            })
        }
    }

    @Test
    fun deleteToDO_success() = runTest {
        val id = 5L
        coEvery { toDoDao.deleteToDO(id) } returns 1

        val result = toDoRepository.deleteToDO(id)

        assertTrue(result)
        coVerify { toDoDao.deleteToDO(id) }
    }

    @Test
    fun getUserIP_success() = runTest {
        val ip = "192.168.1.1"
        coEvery { networkApi.getUserIP() } returns ip

        val result = toDoRepository.getUserIP()

        assertEquals(ip, result)
    }
}