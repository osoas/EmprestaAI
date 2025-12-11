# Empresta Aí

Sistema de empréstimo de objetos entre usuários.

## Tecnologias

-   **Backend**: Java (Spring Boot)
-   **Frontend**: React (Vite)
-   **Banco de Dados**: MySQL (configurado no `application.properties`)

## Como Rodar

### Pré-requisitos

-   Java 21
-   Node.js
-   MySQL

### Backend

1.  Configure o banco de dados MySQL e atualize as credenciais em `src/main/resources/application.properties` se necessário.
2.  Na raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

O servidor iniciará em `http://localhost:8080`.

### Frontend

1.  Navegue até a pasta `front`:

```bash
cd front
```

2.  Instale as dependências:

```bash
npm install
```

3.  Inicie o servidor de desenvolvimento:

```bash
npm run dev
```



