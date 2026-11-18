package com.example.myapplication.ui.telas.telaCriarCriptograma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.remote.model.CitacaoDto
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.EstoicaRepository
import com.example.myapplication.model.enums.Dificuldade
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TelaCriaCriptogramasAdminViewModel(
    private val repository: CriptogramaRepository,
    private val estoicaRepository: EstoicaRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TelaCriaCriptogramasAdminUiState())
    val uiState: StateFlow<TelaCriaCriptogramasAdminUiState> = _uiState.asStateFlow()

    fun carregarCriptogramaPorId(id: Int) {
        if (id <= 0) return
        viewModelScope.launch {
            val cripto = repository.buscarPorId(id)
            if (cripto != null) {
                _uiState.update {
                    it.copy(
                        criptogramaEmEdicao = cripto,
                        frase = cripto.frase,
                        autor = cripto.autor,
                        categoriaSelecionada = cripto.categoria,
                        dificuldadeSelecionada = cripto.dificuldade,
                        origemSelecionada = if (cripto.idExterno?.startsWith("API") == true) "API de citações" else "Manual"
                    )
                }
            }
        }
    }

    fun onFraseChange(novaFrase: String) {
        _uiState.update { it.copy(frase = novaFrase) }
    }

    fun onAutorChange(novoAutor: String) {
        _uiState.update { it.copy(autor = novoAutor) }
    }

    fun onCategoriaSelecionadaChange(novaCategoria: String) {
        _uiState.update { it.copy(categoriaSelecionada = novaCategoria, menuCategoriaExpandido = false) }
    }

    fun onDificuldadeSelecionadaChange(novaDificuldade: Dificuldade) {
        _uiState.update { it.copy(dificuldadeSelecionada = novaDificuldade) }
    }

    fun onOrigemSelecionadaChange(novaOrigem: String) {
        _uiState.update { it.copy(origemSelecionada = novaOrigem) }
    }

    fun onTermoBuscaApiChange(novoTermo: String) {
        _uiState.update { it.copy(termoBuscaApi = novoTermo) }
    }

    fun buscarCitacoesNaApi() {
        val termo = _uiState.value.termoBuscaApi.trim()
        if (termo.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(buscandoApi = true, mensagemErroApi = null) }

            estoicaRepository.buscarCitacoes(termo)
                .onSuccess { citacoes ->
                    _uiState.update {
                        it.copy(
                            buscandoApi = false,
                            resultadoCitacoesApi = citacoes,
                            mensagemErroApi = if (citacoes.isEmpty()) {
                                "Nenhuma citação encontrada para '$termo'."
                            } else null
                        )
                    }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            buscandoApi = false,
                            resultadoCitacoesApi = emptyList(),
                            mensagemErroApi = erro.message
                        )
                    }
                }
        }
    }

    fun selecionarCitacaoApi(citacao: CitacaoDto) {
        _uiState.update {
            it.copy(
                frase = citacao.texto ?: "",
                autor = citacao.autor ?: "",
                resultadoCitacoesApi = emptyList()
            )
        }
    }

    fun onMenuCategoriaExpandidoChange(expandido: Boolean) {
        _uiState.update { it.copy(menuCategoriaExpandido = expandido) }
    }

    fun salvarCriptograma(onSucesso: (Criptograma) -> Unit) {
        _uiState.update { it.copy(tentouSalvar = true) }
        if (_uiState.value.erros.temErros) return

        viewModelScope.launch {
            val cripto = criarCriptograma()
            repository.salvar(cripto)
            onSucesso(cripto)
        }
    }

    private fun criarCriptograma(): Criptograma {
        val state = _uiState.value
        val prefixo = if (state.origemSelecionada == "Manual") "MANUAL" else "API"

        val idExternoFinal = if (state.criptogramaEmEdicao != null) {
            state.criptogramaEmEdicao.idExterno ?: "${prefixo}_${UUID.randomUUID().toString().take(8)}"
        } else {
            "${prefixo}_${UUID.randomUUID().toString().take(8)}"
        }

        return Criptograma(
            id = state.criptogramaEmEdicao?.id ?: 0,
            frase = state.frase,
            autor = state.autor,
            categoria = state.categoriaSelecionada,
            dificuldade = state.dificuldadeSelecionada,
            idExterno = idExternoFinal
        )
    }
}
