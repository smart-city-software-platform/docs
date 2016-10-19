# Roadmap - Resource Adaptor

TODO: TRADUZIR

## Design Roadmap

![ResourceAdaptor](../images/resource_adaptor_roadmap.png)

* Separar Resource Adaptor do código dos recursos
 * Implementar API de sensores


## Definition

O Resource Adaptor é um serviço que interage com os dispositivos físicos por meio de uma API REST (Por enquanto). A ideia é termos bibliotecas em diferentes linguagens de programação que facilite a comunicação dos códigos dos dispositivos com o Resource Adaptor, semelhante ao que o RAILS faz com a comunicação dos banco de dados (Postgresql), oferecendo uma API de alto nível. Essa API vai contemplar:

(ESCREVER AQUI)

Cada recurso é composto por metadados gerais (Tipo, latitude e longitude), comportamentos (Capacidades) e por metadados específicos (Metadata)

## Metadata

* Type
* Description
* UUID
* Lat
* Lon

## Types Capabilities

Capacidades são funções que um recurso oferece, caracterizando o comportamento de um recurso. Essas capacidades podem ser dos seguintes tipos:

* **Periodic Sensor**: sensores que dependem de um período de coleta fixo. Deve-se ter um tipo de dados específico (Definir os tipos de dados) e uma unidade de medida. Ex: sensor de temperatura que coleta dados de uma sala a cada 5 minutos.
* **Actuator**: recebe um comando e altera o seu estado. Ele tem estados aceitáveis ou intervalo de valores. Ex: semáforo que possui três estados possíveis (Verde, amarelo e vermelho)
* **Event-based**: somente registra um dado quando um determinado evento ocorre. Ex: sensor de presença, que somente registra um dado quando alguém está na sala.
* **RFID**: NÃO CONTEMPLADO ATUALMENTE

## Attribute 

Atributos são tipos dados específicos que caracterizam um tipo de recurso. No geral, esses atributos não são modificados com frequência e nem a partir de eventos externos. Por exemplo, um vaga de estacionamento possui os seguintes atributos:

* Número de Identificação dentro do estacionamento
* Se é uma vaga prioritária
* Horário de funcionamento 
* Preço

# Funcionalidades oferecidas pelo Resource Adaptor

Tudo tem que ser oferecido através de uma API que utilize um protocolo de comunicação (ex: Rest) e uma representação de dados (ex: JSON)
Nessa ideia nova, o Data Collector vai parar de ficar coletando os dados do Resource Adatpor e vai passar a receber notificações do Adaptor toda vez que um sensor gerar dado. Assim, o Resource Adaptor não teria um banco de dados normal, apenas um bando de dados em memória para guardar o último dado coletado, comando de atuadores e as relações de Pub/Sub criadas.

API para os dispositivos: 

* **Registrar um recurso**: primeiro passo para o recurso ficar disponível na plataforma. Ele gera um Token único para identificação desse recurso.
* **Atualizar um recurso**: atualizar dados de um recurso já existente.
* **Istanciar um recurso existente**: Autenticar que um recurso é ele mesmo através da chave. De outra forma, o recurso teria que se cadastrar novamente e o recurso antigo se perderia.
* **Enviar dados coletados**: Informando a capacidade, tempo da coleta e o dado coletado. Essa API pode ser utilizada tanto para capacidades periódicas, quanto por capacidades que geram eventos
* **Notificação de atuadores**: Tem que prover um PUB/SUB para que atuadores recebam notificações toda vez que surgir uma solicitação de mudança de estado.
* **Função para verificar/atualizar o status de um recurso**
* **Api para guardar atributos específicos**:  Provavelmente em uma estrutura NoSQL como o MongoDB



