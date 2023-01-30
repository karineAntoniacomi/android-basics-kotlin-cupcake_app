package com.example.cupcake

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cupcake.model.OrderViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ViewModelTests {

    // Regra que especifica que os objetos LiveData não podem chamar a linha de execução principal
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // teste que verifica se o objeto quantity no OrderViewModel está atualizado quando setQuantity é chamado
    @Test
    fun quantity_twelve_cupcakes() {
        val viewModel = OrderViewModel()
        // Os objetos LiveData precisam ser observados para que as mudanças sejam emitidas
        viewModel.quantity.observeForever {}
        viewModel.setQuantity(12)
        assertEquals(12, viewModel.quantity.value)
    }

    @Test
    fun price_twelve_cupcakes() {
        val viewModel = OrderViewModel()
        viewModel.setQuantity(12)
        viewModel.price.observeForever {}
        assertEquals("R\$ 27,00", viewModel.price.value)
    }
}