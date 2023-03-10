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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentSummaryBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [SummaryFragment] contains a summary of the order details with a button to share the order
 * via another app.
 */
class SummaryFragment : Fragment() {

    // Binding object instance corresponding to the fragment_summary.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentSummaryBinding? = null

    // acessa uma referencia ao modelo de visualizacao compartilhada como variavel de classe
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
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
            //sendButton.setOnClickListener { sendOrder() }
            summaryFragment = this@SummaryFragment
        }
    }

    /**
     * Submit the order by sharing out the order details to another app via an implicit intent.
     */
    fun sendOrder() {
       // Toast.makeText(activity, "Send Order", Toast.LENGTH_SHORT).show()
        // string formatada com os detalhes do pedido a ser enviado
        val orderSummary = getString(
            R.string.order_details,
            sharedViewModel.quantity.value.toString(),
            sharedViewModel.flavor.value.toString(),
            sharedViewModel.date.value.toString(),
            sharedViewModel.price.value.toString()
        )
        // Cria uma intent impl??cita para compartilhar o pedido com outro app
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
                // Intent.EXTRA_SUBJECT = string com o assunto do e-mail
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
                // Intent.EXTRA_TEXT = string com o corpo do e-mail
            .putExtra(Intent.EXTRA_TEXT, orderSummary)
            .putExtra(Intent.EXTRA_EMAIL, "lucas.rafalski1@gmail.com")

        // Evita que o app Cupcake falhe se n??o houver um app que possa processar a intent
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }
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
        findNavController().navigate(R.id.action_summaryFragment_to_startFragment)
    }
}