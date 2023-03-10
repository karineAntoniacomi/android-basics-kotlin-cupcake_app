/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentPickupBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [PickupFragment] allows the user to choose a pickup date for the cupcake order.
 */
class PickupFragment : Fragment() {

    // Binding object instance corresponding to the fragment_pickup.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentPickupBinding? = null

    // acessa uma referencia ao modelo de visualizacao compartilhada como variavel de classe
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentPickupBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            // Atualiza os elementos da IU automaticamente pela associa????o com binding.
            // App poder?? observar objetos LiveData
            // Define o propriet??rio do ciclo de vida no objeto de vincula????o.
            lifecycleOwner = viewLifecycleOwner

            viewModel = sharedViewModel

            // C??digo que define manualmente o listener, removido p/ usar vincula????o de dados.
            // nextButton.setOnClickListener { goToNextScreen() }
            pickupFragment = this@PickupFragment
        }
    }

    /**
     * Navigate to the next screen to see the order summary.
     */
    fun goToNextScreen() {
        // Toast.makeText(activity, "Next", Toast.LENGTH_SHORT).show()
        // Navega at?? o fragmento de resumo
        findNavController().navigate(R.id.action_pickupFragment_to_summaryFragment)
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // A????o do Bot??o cancelar pedido: limpa o modelo de visualiza????o chamando o m??todo resetOrder()
    fun cancelOrder() {
        sharedViewModel.resetOrder()
        // retorna ao StartFragment usando a a????o de navega????o com i iD
        findNavController().navigate(R.id.action_pickupFragment_to_startFragment)
    }
}