
# Teste Gabriel - Quarkus

Este é um projeto de exemplo construído com **Quarkus**, utilizando **Hibernate Panache**, **RESTEasy**, banco de dados **H2 em memória**, e **Java 17**. O projeto realiza o controle de filmes indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards.

Ao iniciar a aplicação, um arquivo CSV será lido automaticamente para popular o banco de dados com os filmes indicados.

A aplicação disponibiliza endpoints REST para a manutenção dos registros de filmes e também para obter o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido.

#### Local do arquivo CSV
```
/src/main/resources/Movielist.csv
```

## Tecnologias Utilizadas

- [Quarkus 3.23.3](https://quarkus.io/)
- Java 17
- Maven
- Hibernate ORM Panache
- H2 Database
- RESTEasy com Jackson
- JUnit 5 e RestAssured (para testes)
- SLF4J (para logging)

## Requisitos

- Java 17+
- Maven 3.8+

## Como executar o projeto

Clone este repositório:

```bash
git clone https://github.com/gabrielfrna/teste-gabriel-quarkus.git
cd teste-gabriel-quarkus
```

### Executar em modo dev

Executar com Maven instalado:

```bash
mvn quarkus:dev
```

A aplicação estará disponível em: [http://localhost:8080](http://localhost:8080)

## Executando os testes

```bash
mvn test
```

## Endpoints disponíveis

### Listar todos os Filmes

```http
GET /api/movie
```

#### Exemplo de retorno
```json
[
    {
        "id": 1,
        "movieYear": 1980,
        "title": "Can't Stop the Music",
        "studios": "Associated Film Distribution",
        "producers": "Allan Carr",
        "winner": true
    },
    {
        "id": 2,
        "movieYear": 1980,
        "title": "Cruising",
        "studios": "Lorimar Productions, United Artists",
        "producers": "Jerry Weintraub",
        "winner": false
    }
]
```

### Filtrar um filme pelo Id

```http
GET /api/movie/{id}
```

#### Exemplo de retorno
```json
{
    "id": 200,
    "movieYear": 2018,
    "title": "Robin Hood",
    "studios": "Summit Entertainment",
    "producers": "Jennifer Davisson and Leonardo DiCaprio",
    "winner": false
}
```

### Cadastrar um Filme

```http
POST /api/movie
```

#### Exemplo de requisão no Body
```json
{
    "movieYear": 2025,
    "title": "Filme 10",
    "studios": "Estúdio 1",
    "producers": "Producer 2",
    "winner": true
}
```

### Editar um Filme

```http
PUT /api/movie/{id}
```

#### Exemplo de requisão no Body
```json
{
    "id": 208,
    "movieYear": 2010,
    "title": "Filme após alterado",
    "studios": "Estúdio alterado",
    "producers": "Produtor também alterado",
    "winner": false
}
```

### Remover um Filme

```http
DELETE /api/movie/{id}
```

#### Exemplo de retorno
```
Filme removido com sucesso
```

### Obter o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido

```http
GET /api/movie/award-intervals
```

#### Exemplo de retorno
```json
{
    "min": [
        {
            "followingWin": 1991,
            "producer": "Joel Silver",
            "interval": 1,
            "previousWin": 1990
        }
    ],
    "max": [
        {
            "followingWin": 2015,
            "producer": "Matthew Vaughn",
            "interval": 13,
            "previousWin": 2002
        }
    ]
}
```

## Comandos úteis

### Gerar build:

```bash
mvn clean install
```

## Estrutura do Projeto

```bash
src
├── main
│   ├── java
│   │   └── com.teste
│   │       ├── config       # Configurações
│   │       ├── controller   # Endpoints REST
│   │       ├── model        # Entidades
│   │       ├── repository   # Repositórios
│   │       └── service      # Regras de negócio
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com.teste        # Testes de integração
```

---

## Autor

Gabriel — desenvolvimento para fins de teste técnico com Quarkus.

---
