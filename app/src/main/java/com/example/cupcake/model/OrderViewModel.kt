package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// preço por Cupcake - constante privada somente leitura
private const val PRICE_PER_CUPCAKE = 2.00

// constante de nível superior para custo extra de retirada no mesmo dia
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {

    // Valores iniciais da declaração de propriedades na classe (substituídos pela fun resetOrder())
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    // Possible date options
    val dateOptions: List<String> = getPickupOptions()

    // Pickup date
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    //val price: LiveData<Double> = _price
    val price: LiveData<String> = Transformations.map(_price) {
        // Transformations.map() inicializa a nova variável. Método getCurrencyInstance()
        // na classe NumberFormat converte o preço no formato da moeda local.
        NumberFormat.getCurrencyInstance().format(it)
    }

    // bloco init inicializa as propriedades quando uma instância do OrderViewModel é criada
    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int){
        //atribui o argumento transmitido para as propriedades  mutáveis
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String){
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String){
        _date.value = pickupDate
        // Chama updatePrice() para adicionar as cobranças de retirada no mesmo dia
        updatePrice()
    }

    //  Cria e retorna a lista de datas de retirada
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()  // variável que conterá a data e hora atuais

        // Cria lista de datas (4 opções) começando com a data atual e as três datas seguintes
        repeat(4) {
            // formata data, adiciona-a à lista de opções e incrementa a agenda em um dia
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    // Redefine propriedades MutableLiveData no modelo de visualização
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]   // Atribui valor atual da data da lista dateOptions
        _price.value = 0.0
    }

    // verifica se o sabor do pedido foi definido ou não
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    // método auxiliar para calcular o preço total de cupcakes
    private fun updatePrice() {
        // Multiplica a quantidade de cupcakes pedidos pelo preço por cupcake. Como o valor de
        // quantity.value pode ser nulo, usa-se o operador elvis (?:) que significa que se a expressão
        // à esquerda não for nula, será usada. Se for nula, usa-se a expressão 0 à direita.
        // _price.value = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE

        // Verifica se o usuário selecionou a retirada no mesmo dia. Se a data no modelo de
        // visualização (_date.value) é igual ao primeiro item da lista de dateOptions (dia atual)
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

}