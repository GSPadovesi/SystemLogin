# SystemLogin

## Ficha tecnica

Projeto backend para autenticacao e autorizacao de usuarios, implementado em Java com Spring Boot. A aplicacao expoe endpoints REST para cadastro, login, validacao de JWT via filtro de seguranca e alteracao de perfil de usuario.

## Tecnologias

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Maven
- BCrypt para hash de senha
- JWT com `com.auth0:java-jwt`
- `dotenv-java` para carregamento de variaveis de ambiente a partir de `.env`

## Modulo principal

O codigo fonte esta no diretorio `login`.

Estrutura principal:

- `br.com.login.domain`: modelo de dominio e enum de perfil.
- `br.com.login.application`: casos de uso, comandos e gateways.
- `br.com.login.infrastructure`: configuracoes, seguranca, persistencia, adapters e excecoes.
- `br.com.login.presentation`: controllers REST, DTOs e tratamento global de excecao.

## Dominio

### `User`

Entidade de dominio usada pela aplicacao.

Campos:

- `id`: UUID do usuario.
- `username`: nome do usuario.
- `email`: email usado como identificador de autenticacao.
- `password`: senha armazenada ja com hash.
- `role`: perfil do usuario.

Metodos principais:

- `create`: cria um novo usuario com role padrao `USER`.
- `restore`: reconstrui um usuario existente vindo da persistencia.
- `changeUsername`: altera o nome.
- `changePassword`: altera a senha.
- `changeRole`: altera o perfil.

### `UserRole`

Perfis disponiveis:

- `ADMIN`
- `USER`

## Camada de aplicacao

### Commands

- `RegisterCommand`: transporta `username`, `email` e `password` para o cadastro.
- `LoginCommand`: transporta `email` e `password` para login.
- `RoleCommand`: transporta `role` e `id` para alteracao de perfil.
- `UserCommand`: representa dados completos de usuario.

### Gateways

- `AuthGateway`: define contrato para hash de senha, geracao de token e validacao de senha.
- `UserGateway`: define contrato para salvar usuario, buscar por email, buscar por id e verificar existencia por email.

### Casos de uso

#### `RegisterUseCase`

Fluxo executado:

1. Verifica se ja existe usuario com o email informado.
2. Gera hash da senha usando `AuthGateway`.
3. Cria usuario de dominio com role padrao `USER`.
4. Salva o usuario via `UserGateway`.
5. Gera um JWT para o usuario salvo.
6. Retorna o token.

#### `LoginUseCase`

Fluxo executado:

1. Busca usuario pelo email.
2. Compara a senha enviada com a senha armazenada em hash.
3. Gera um JWT se a senha for valida.
4. Retorna o token.

#### `ChangeRoleUserUseCase`

Fluxo executado:

1. Busca usuario pelo UUID.
2. Verifica se a role enviada e diferente da role atual.
3. Atualiza a role do usuario.
4. Salva a alteracao.
5. Retorna `true`.

## Infraestrutura

### Persistencia

A persistencia usa Spring Data JPA com PostgreSQL.

Entidade JPA: `UserEntity`

Tabela: `users`

Colunas:

- `id`: UUID, chave primaria, gerado automaticamente.
- `username`: texto obrigatorio, unico, tamanho maximo 120.
- `email`: texto obrigatorio, unico, tamanho maximo 180.
- `password`: texto obrigatorio, tamanho maximo 100.
- `role`: enum armazenado como string, obrigatorio.
- `created_at`: data de criacao.
- `updated_at`: data de atualizacao.

Componentes:

- `UserRepository`: repository JPA com busca por email e verificacao de email existente.
- `UserEntityMapper`: converte entre `User` e `UserEntity`.
- `UserPersistenceAdapter`: implementa `UserGateway` usando repository e mapper.

### Autenticacao

`AuthPersistenceAdapter` implementa `AuthGateway`.

Responsabilidades:

- Gerar hash de senha com `PasswordEncoder`.
- Validar senha com BCrypt.
- Gerar JWT assinado com HMAC256.

Claims do token:

- `sub`: email do usuario.
- `role`: role do usuario.
- `userId`: UUID do usuario.
- `iss`: `authentication-api`.
- Expiracao: 2 horas a partir da geracao.

### Filtro JWT

`JwtAuthenticationFilter` estende `OncePerRequestFilter`.

Fluxo executado:

1. Le o header `Authorization`.
2. Ignora a requisicao se o header nao existir ou nao iniciar com `Bearer `.
3. Valida o JWT com o secret configurado.
4. Busca o usuario pelo email presente no subject do token.
5. Cria `UsernamePasswordAuthenticationToken` com as authorities do usuario.
6. Define a autenticacao no `SecurityContextHolder`.
7. Retorna HTTP 401 quando o token e invalido.

### Usuario autenticado

`UserAuthenticated` implementa `UserDetails`.

Authority gerada:

- `ROLE_` + nome da role do usuario.

Exemplo:

- `USER` vira `ROLE_USER`.
- `ADMIN` vira `ROLE_ADMIN`.

## Configuracoes

### `application.properties`

Configuracoes usadas pela aplicacao:

```properties
spring.application.name=login
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.url=${DB_URL}
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret.api=${JWT_SECRET_KEY}
```

### Variaveis de ambiente

O projeto possui `.env.example` com as variaveis esperadas:

```env
DB_USERNAME=postgres
DB_PASSWORD=change-me
DB_URL=url_db
JWT_SECRET_KEY=string_segredo_aqui
```

O carregamento do `.env` e feito por `DotenvConfig`, registrado em:

```properties
org.springframework.boot.env.EnvironmentPostProcessor=br.com.login.infrastructure.config.DotenvConfig
```

Arquivo:

- `src/main/resources/META-INF/spring.factories`

## Seguranca

Configuracao principal: `SecurityConfig`

Regras:

- `POST /auth/**`: publico.
- `/admin` e `/admin/**`: exige role `ADMIN`.
- `POST /user/{id}`: exige role `ADMIN`.
- `/user` e `/user/**`: exige role `ADMIN` ou `USER`.
- Demais rotas: exigem autenticacao.

Configuracoes adicionais:

- CSRF desabilitado.
- Sessao stateless.
- Form login desabilitado.
- `JwtAuthenticationFilter` executa antes de `UsernamePasswordAuthenticationFilter`.

## Endpoints

### Cadastro

`POST /auth/register`

Body:

```json
{
  "username": "usuario",
  "email": "usuario@email.com",
  "password": "senha"
}
```

Resposta de sucesso:

- Status: `201 Created`
- Body:

```json
{
  "token": "jwt"
}
```

### Login

`POST /auth/login`

Body:

```json
{
  "email": "usuario@email.com",
  "password": "senha"
}
```

Resposta de sucesso:

- Status: `200 OK`
- Body:

```json
{
  "token": "jwt"
}
```

### Rota de usuario

`GET /user`

Autorizacao:

- `ADMIN`
- `USER`

Resposta:

```text
User
```

### Rota de administrador

`GET /admin`

Autorizacao:

- `ADMIN`

Resposta:

```text
ADMIN
```

### Alterar role de usuario

`POST /user/{id}`

Autorizacao:

- `ADMIN`

Body:

```json
{
  "userRole": "ADMIN"
}
```

ou:

```json
{
  "userRole": "USER"
}
```

Resposta de sucesso:

- Status: `200 OK`
- Body:

```json
true
```

## Tratamento de excecao

`GlobalExceptionHandler` trata `TokenGenerationException`.

Resposta retornada:

```json
{
  "timestamp": "data-hora",
  "status": 500,
  "error": "Internal Server Error",
  "message": "mensagem da excecao"
}
```

## Como executar

Entre no modulo da aplicacao:

```bash
cd login
```

Crie um arquivo `.env` com base no `.env.example`:

```env
DB_USERNAME=postgres
DB_PASSWORD=change-me
DB_URL=jdbc:postgresql://localhost:5432/nome_do_banco
JWT_SECRET_KEY=string_segredo_aqui
```

Execute a aplicacao:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## Testes

Executar testes:

```bash
./mvnw test
```

No Windows PowerShell:

```powershell
.\mvnw.cmd test
```

Teste existente:

- `LoginApplicationTests.contextLoads`: valida carregamento do contexto Spring.

## Build

Gerar build com Maven:

```bash
./mvnw clean package
```

No Windows PowerShell:

```powershell
.\mvnw.cmd clean package
```
