import { useState, useEffect } from 'react'
import axios from 'axios'

function App() {
  const anoAtual = new Date().getFullYear()
  const [agendamentos, setAgendamentos] = useState([])
  const [nomePaciente, setNomePaciente] = useState('')
  const [telefonePaciente, setTelefonePaciente] = useState('')
  const [dataHora, setDataHora] = useState('')
  const [mensagem, setMensagem] = useState('')

  useEffect(() => {
    carregarAgendamentos()
  }, [])

  const carregarAgendamentos = () => {
    axios.get('http://localhost:8081/api/agendamentos')
      .then(resposta => {
        setAgendamentos(resposta.data)
      })
      .catch(erro => {
        console.error("Erro ao carregar a agenda:", erro)
      })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    setMensagem('')

    const dataFormatada = dataHora.length === 16 ? `${dataHora}:00` : dataHora

    const novoAgendamento = {
      nomePaciente,
      telefonePaciente,
      dataHora: dataFormatada
    }

    axios.post('http://localhost:8081/api/agendamentos', novoAgendamento)
      .then(() => {
        setMensagem('Agendamento realizado com sucesso!')
        setNomePaciente('')
        setTelefonePaciente('')
        setDataHora('')
        carregarAgendamentos()
      })
      .catch(erro => {
        if (erro.response && erro.response.data) {
          const mensagemServidor = erro.response.data.message || erro.response.data.error || 'Horário indisponível.'
          setMensagem(`Erro: ${mensagemServidor}`)
        } else {
          setMensagem('Erro de conexão com o servidor.')
        }
      })
  }

  // --- NOVAS FUNÇÕES DOS BOTÕES ---
  const alterarStatus = (id, novoStatus) => {
    // O axios agora cuida de enviar os parâmetros de forma segura (URL Encode)
    axios.patch(`http://localhost:8081/api/agendamentos/${id}/status`, null, {
      params: { status: novoStatus }
    })
      .then(() => {
        carregarAgendamentos()
        setMensagem('') // Limpa qualquer erro anterior da tela
      })
      .catch(erro => {
        // Agora também mostramos o erro na tela, igual no formulário!
        if (erro.response && erro.response.data && erro.response.data.message) {
          setMensagem(`Erro ao atualizar: ${erro.response.data.message}`)
        } else {
          setMensagem('Erro de conexão ou validação ao tentar atualizar o status.')
        }
      })
  }
  
  const deletarAgendamento = (id) => {
    if (window.confirm("Tem certeza que deseja apagar esta consulta?")) {
      axios.delete(`http://localhost:8081/api/agendamentos/${id}`)
        .then(() => carregarAgendamentos())
        .catch(erro => console.error("Erro ao deletar:", erro))
    }
  }

  return (
    <div style={{ padding: '30px', fontFamily: 'system-ui, sans-serif', maxWidth: '1000px', margin: '0 auto' }}>
      <h1 style={{ color: '#333' }}>Agenda do Dentista</h1>
      <p style={{ color: '#666', marginBottom: '30px' }}>Sistema de gestão de consultas</p>

      <div style={{ 
        backgroundColor: '#f8f9fa', 
        padding: '20px', 
        borderRadius: '10px', 
        marginBottom: '40px',
        border: '1px solid #e9ecef'
      }}>
        <h2 style={{ marginTop: 0, fontSize: '1.2rem', color: '#495057' }}>Novo Agendamento</h2>
        
        {mensagem && (
          <div style={{ 
            padding: '10px', 
            marginBottom: '15px', 
            borderRadius: '5px',
            backgroundColor: mensagem.startsWith('Erro') ? '#f8d7da' : '#d4edda',
            color: mensagem.startsWith('Erro') ? '#721c24' : '#155724'
          }}>
            {mensagem}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'grid', gap: '15px', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '5px', fontSize: '0.9rem' }}>Nome do Paciente:</label>
            <input 
              type="text" 
              required
              value={nomePaciente}
              onChange={(e) => setNomePaciente(e.target.value)}
              style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc', boxSizing: 'border-box' }}
              placeholder="Ex: Ana Silva"
            />
          </div>

          <div>
            <label style={{ display: 'block', marginBottom: '5px', fontSize: '0.9rem' }}>Telefone (WhatsApp):</label>
            <input 
              type="text" 
              required
              value={telefonePaciente}
              onChange={(e) => setTelefonePaciente(e.target.value)}
              style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc', boxSizing: 'border-box' }}
              placeholder="Ex: 11999998888"
            />
          </div>

          <div>
            <label style={{ display: 'block', marginBottom: '5px', fontSize: '0.9rem' }}>Data e Horário:</label>
            <input 
              type="datetime-local"
              max={`${anoAtual}-12-31T23:59`}
              required
              value={dataHora}
              onChange={(e) => setDataHora(e.target.value)}
              style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc', boxSizing: 'border-box' }}
            />
          </div>

          <div style={{ gridColumn: '1 / -1' }}>
            <button 
              type="submit" 
              style={{ 
                padding: '10px 20px', 
                backgroundColor: '#0056b3', 
                color: 'white', 
                border: 'none', 
                borderRadius: '5px', 
                fontWeight: 'bold',
                cursor: 'pointer'
              }}
            >
              Agendar Consulta
            </button>
          </div>
        </form>
      </div>

      <h2>Agendamentos Confirmados</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px' }}>
        {agendamentos.map(agendamento => (
          <div key={agendamento.id} style={{ 
            border: '1px solid #e0e0e0', 
            padding: '20px', 
            borderRadius: '10px',
            boxShadow: '0 4px 6px rgba(0,0,0,0.05)',
            backgroundColor: '#ffffff'
          }}>
            <h3 style={{ marginTop: '0', color: '#0056b3' }}>{agendamento.nomePaciente}</h3>
            <p style={{ margin: '5px 0' }}><strong>Telefone:</strong> {agendamento.telefonePaciente}</p>
            <p style={{ margin: '5px 0' }}><strong>Data/Hora:</strong> {new Date(agendamento.dataHora).toLocaleString('pt-BR')}</p>
            
            <span style={{ 
              display: 'inline-block', 
              padding: '4px 8px', 
              backgroundColor: agendamento.status === 'CONCLUÍDO' ? '#d4edda' : agendamento.status === 'CANCELADO' ? '#f8d7da' : '#fff3cd', 
              color: agendamento.status === 'CONCLUÍDO' ? '#155724' : agendamento.status === 'CANCELADO' ? '#721c24' : '#856404', 
              borderRadius: '4px',
              fontSize: '12px',
              fontWeight: 'bold',
              marginTop: '5px'
            }}>
              {agendamento.status}
            </span>
            
            <div style={{ marginTop: '15px' }}>
              {agendamento.linkWhatsapp && (
                <a 
                  href={agendamento.linkWhatsapp} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  style={{ 
                    display: 'block', 
                    textAlign: 'center',
                    padding: '8px', 
                    backgroundColor: '#25D366', 
                    color: 'white', 
                    textDecoration: 'none', 
                    borderRadius: '6px',
                    fontWeight: 'bold',
                    fontSize: '0.9rem',
                    marginBottom: '10px'
                  }}
                >
                  WhatsApp
                </a>
              )}
            </div>

            {/* --- AQUI FICAM OS NOVOS BOTÕES DE AÇÃO --- */}
            <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
              <button 
                onClick={() => alterarStatus(agendamento.id, 'CONCLUÍDO')}
                style={{ flex: 1, padding: '8px', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '0.85rem' }}>
                ✔ Concluir
              </button>
              <button 
                onClick={() => alterarStatus(agendamento.id, 'CANCELADO')}
                style={{ flex: 1, padding: '8px', backgroundColor: '#ffc107', color: '#333', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '0.85rem' }}>
                ✖ Cancelar
              </button>
              <button 
                onClick={() => deletarAgendamento(agendamento.id)}
                style={{ flex: 1, padding: '8px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '0.85rem' }}>
                🗑 Excluir
              </button>
            </div>

          </div>
        ))}
      </div>
    </div>
  )
}

export default App