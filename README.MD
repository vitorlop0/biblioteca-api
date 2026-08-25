# Library API

API REST para gerenciamento de acervo de livros (cadastro, consulta, atualização e remoção), construída com Spring Boot seguindo boas práticas de arquitetura em camadas.

## Tecnologias

- Java 17
- Spring Boot (Web, Validation, Data JPA)
- Hibernate
- PostgreSQL
- Gradle
- JUnit 5 + Mockito (testes)
- Lombok
- Swagger / OpenAPI (springdoc-openapi)

## Arquitetura

O projeto segue uma separação clara de responsabilidades, evitando expor a entidade de persistência diretamente na API:

```
controller/   -> recebe as requisições HTTP e delega para o service
service/      -> regras de negócio
repository/   -> acesso a dados (Spring Data JPA)
model/        -> entidade JPA (Livro)
dto/          -> contratos da API (LivroRequest / LivroResponse)
mapper/       -> conversão entre entidade e DTO
exceptions/   -> exceções de negócio e tratamento global de erros
```

## Endpoints

| Método | Rota          | Descrição                     | Body            |
|--------|---------------|--------------------------------|-----------------|
| GET    | `/livros`     | Lista todos os livros          | -               |
| GET    | `/livros/{id}`| Busca um livro por id          | -               |
| POST   | `/livros`     | Cadastra um novo livro         | `LivroRequest`  |
| PUT    | `/livros/{id}`| Atualiza um livro existente    | `LivroRequest`  |
| DELETE | `/livros/{id}`| Remove um livro                | -               |
| POST   | `/livros/{id}/emprestar` | Marca o livro como emprestado | -   |
| POST   | `/livros/{id}/devolver`  | Marca o livro como devolvido  | -   |

**LivroRequest** (entrada):
```json
{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "anoPublicacao": 1899
}
```

**LivroResponse** (saída):
```json
{
  "id": 1,
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "anoPublicacao": 1899,
  "disponivel": true
}
```

## Tratamento de erros

Todos os erros retornam um corpo padronizado, em vez de mensagens soltas ou stack traces:

```json
{
  "message": "Livro não encontrado",
  "status": 404,
  "timestamp": "2026-08-24T10:15:30"
}
```

- `404` quando o livro não existe
- `400` quando a validação dos campos falha (ex: título ou autor em branco)
- `409` quando a operação de empréstimo/devolução é inválida para o estado atual do livro (ex: emprestar um livro já emprestado, ou devolver um que já está disponível)
- `500` como fallback genérico para qualquer exceção não tratada, sem vazar detalhes internos

## Documentação interativa (Swagger)

Com a aplicação rodando, a documentação de todos os endpoints fica disponível em:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## O que diferencia este projeto

Pontos de atenção que fui aplicando conscientemente à medida que o projeto evoluiu:

- **DTOs em vez da entidade exposta na API** — `LivroRequest`/`LivroResponse` desacoplam o contrato HTTP do modelo de persistência, evitando que mudanças no banco quebrem o consumidor da API.
- **Mapper centralizado** — a conversão `Livro -> LivroResponse` vive em um único lugar (`LivroMapper`), eliminando duplicação que existia antes em cada método do service.
- **Tratamento global de exceções** — `@RestControllerAdvice` centraliza as respostas de erro, incluindo um handler genérico que evita expor stack traces ao cliente.
- **Validação de entrada** — Bean Validation (`@NotBlank`) com mensagens de erro específicas por campo.
- **Regra de negócio de empréstimo/devolução** — o campo `disponivel` não é só um dado estático: os endpoints de emprestar/devolver controlam essa transição de estado e rejeitam operações inválidas (ex: emprestar um livro que já está emprestado) com `409 Conflict`.
- **Testes unitários e de controller** — o `LivroService` é testado isoladamente com Mockito (cadastro, busca, listagem, atualização, remoção e empréstimo/devolução, incluindo caminhos de erro), e o `LivroController` tem testes com `MockMvc` cobrindo os status HTTP de cada cenário (200, 400, 404, 409).
- **Configuração sem credenciais hardcoded** — usuário e senha do banco vêm de variáveis de ambiente (`DB_USERNAME`, `DB_PASSWORD`).
- **API autodocumentada com Swagger/OpenAPI** — cada endpoint é anotado (`@Tag`/`@Operation`), permitindo explorar e testar a API direto pelo navegador, sem depender de coleções externas (ex: Postman).

## Como rodar localmente

Pré-requisitos: JDK 17 e um PostgreSQL rodando localmente com um banco `library_db`.

```bash
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha

./gradlew bootRun
```

A API sobe em `http://localhost:8080`.

## Rodando os testes

```bash
./gradlew test
```

## Autor

Desenvolvido por [Vitor Lopes](https://github.com/vitorlop0).
