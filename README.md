<h1 align="center">Prova desenvolvimento</h1>

<p align="center">Simples aplicação de pedidos.</p>

<br>
<br>

<h1 align="center">Tecnologias utilizadas</h1>
- Java 20 </br>
- Spring-boot 3 </br>
- Banco h2 e Postgres  </br>
- Docker  </br>
- Postman  </br>

 </br>


<h1 align="center"> Solicitações </h1>

- [x] cria pedido
- [x] Adicionar produto no pedido
- [x] Retirar produto do pedido
- [x] Calcular o preço total do pedido
- [x] Fechar o pedido
- [ ] Calcular o preço total do pedido, passando os parâmetros: id do pedido, lista com os
  ids dos produtos e quantidade dos produtos. Deve retornar o preço total do pedido,
  levando em consideração os produtos e quantidades passados como parâmetro.

</br>

# Banco de dados

## H2
Segue endereço para console do H2 (dados de acesso padrão):

http://localhost:8080/h2-console </br>
user: sa </br>
pass: password

No banco H2 ao carregar a aplicação e populado de forma automatica itens da tabela products.

## Postgres

Para usar o postgres deve mudar o arquivo application.properties para usar o ambiente `prd`. </br>

comando para subir a image docker postgres: ``` docker-compose up -d```

No postgres não consegue fazer o mesmo import dos produtos, então criei um endpoint para criar produtos.


## Postman
Na raiz do projeto existe um arquivo `netpricision.postman_collection.json` que é uma coleção de requisições do postman.