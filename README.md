
# 📦 Estoque IGE

Sistema de gerenciamento de estoque desenvolvido no âmbito acadêmico da **Universidade Federal do Sul e Sudeste do Pará**. O objetivo da aplicação é possibilitar maior controle e organização dos materiais utilizados no campus universitário, oferecendo uma interface prática e eficiente para o cadastro, consulta e movimentação de itens em estoque.

## 📖 Índice

- [Introdução](#introdução)
- [Contexto e Problema](#contexto-e-problema)
- [Como Funciona](#como-funciona)
- [Recursos](#recursos)
- [Instalação](#instalação)
- [Instalação por Docker](#docker)
- [Uso](#uso)
- [Configuração de Variáveis de Ambiente](#configuração-de-variáveis-de-ambiente)
- [Diferença entre application.properties dev e prod](#diferença-entre-applicationproperties-dev-e-prod)
- [Desenvolvimento Futuro](#desenvolvimento-futuro)
- [Licença](#licença)
- [Contribuidores](#contribuidores)

## 📌 Introdução

O **Estoque IGE** é um sistema desenvolvido para auxiliar na gestão de estoque de materiais institucionais. Construído como projeto acadêmico, ele explora tecnologias modernas para oferecer um ambiente funcional e de fácil uso, tanto para usuários administrativos quanto para a gestão acadêmica.

## 🎯 Contexto e Problema

A gestão através de planilhas de materiais em instituições de ensino superior pode gerar diversos problemas, como perdas de itens, dificuldade de rastreamento e falhas na reposição de estoque. Pensando nisso, este projeto surgiu com a proposta de informatizar e organizar esse processo no campus da **UNIFESSPA**, proporcionando mais segurança, controle e agilidade no gerenciamento dos materiais.

## ⚙️ Como Funciona

A aplicação é dividida em duas partes principais, separadas por duas pastas:

- **Backend**: desenvolvido em **Java**, utilizando o framework **Spring Boot** e o padrão de arquitetura **MVC (Model-View-Controller)**.  
  Navegando até o caminho `src/main/java/com/estoqueige/estoqueige`, existirá sete pacotes que fazem o gerenciamento do banco de dados e backend:

  - `models`: Representa as entidades do domínio.
  - `repositories`: Interfaces responsáveis pelo acesso a dados.
  - `services`: Regras de negócio intermediando controllers e repositórios.
  - `controllers`: Controladores das rotas HTTP.
  - `dto`: Classes para transferências de dados.
  - `exceptions`: Tratamento de exceções customizadas.
  - `configs`: Configurações específicas do Spring.
  - `security`: Configuração e controle de segurança.

- **Frontend**: construído com **React**, **Next.js**, **TypeScript** e estilização com **Tailwind CSS**. O frontend consome os serviços do backend via API REST.

A aplicação permite:

- Cadastro e gerenciamento de usuários
- Definição de permissões e níveis de acesso
- Cadastro de produtos
- Consulta de itens em estoque
- Cadastro e gerenciamento de unidades e categorias
- Atualização de informações
- Registro de entradas e saídas de materiais
- Cadastro e gerenciamento de requisitantes e cursos

## 🛠️ Recursos

- Sistema web responsivo e moderno
- Backend com **Spring Boot** e arquitetura MVC
- Frontend em **React/Next.js** com **TypeScript** e **Tailwind CSS**
- API REST para integração frontend e backend
- Controle de estoque com cadastro, atualização e movimentação de materiais

## 📥 Instalação

### Pré-requisitos

- Java 17+
- Node.js 18+
- NPM ou Yarn
- MySQL

### Passos para instalar

1. Clone o repositório:
   ```bash
   https://github.com/GabrielMartins404/sistema_estoque_ige.git
   ```

2. Navegue até `src/main/java/com/estoqueige/estoqueige/EstoqueIgeApplication.java` e execute.

3. Na pasta do frontend, instale as dependências:
   ```bash
   npm install
   ```

4. Rode o frontend:
   ```bash
   npm run dev
   ```

5. Acesse: `http://localhost:3000`

**OBS:** Backend padrão na porta `8080` e CORS configurado para aceitar requisições da porta `3000`. Contudo, essas informações poderão ser alteradas via variaveis de ambiente presente no `.env`. Em desenvolvimento, utiliza-se as credenciais acima, contudo, em produção, esses valores deverão ser editados via variaveis de ambiente.

## 📥 Instalação por Docker

### Pré-requisitos

- Docker
- Docker Compose

### Passos

1. Clone o repositório:
   ```bash
   https://github.com/GabrielMartins404/sistema_estoque_ige.git
   ```

2. Na raiz do projeto (onde está o `docker-compose.yml`):
   ```bash
   docker-compose up --build
   ```

Para finalizar:
```bash
docker-compose down
```

## 🔐 Configuração de Variáveis de Ambiente

**⚠️ Atenção:**  
O arquivo `.env` **não deve ser versionado**. Ele deve constar no `.gitignore` e ser compartilhado de forma privada com os desenvolvedores. Entrentanto, para fins de documentação, há um arquivo denominado `.env.dist` a qual traz exemplos de como deve ser configurado o `.env`.

### 📦 Backend (Spring Boot)

| Nome                        | Descrição                                              | Exemplo                                    |
|:---------------------------|:------------------------------------------------------|:--------------------------------------------|
| `SPRING_DATASOURCE_URL`      | URL de conexão com o banco de dados                   | `jdbc:mysql://localhost:3306/estoqueige`   |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco de dados                              | `root`                                     |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco de dados                                | `admin`                                    |
| `USER_LOGIN`                 | E-mail padrão do administrador                        | `suporte@teste`                        |
| `USER_PASSWORD`              | Senha padrão do administrador                         | `123`                           |
| `JWT_SECRET`                 | Chave secreta para JWT                                 | `superSegredo123456789`                    |
| `JWT_EXPIRATION`             | Tempo de expiração do JWT em milissegundos             | `86400000`                                 |
| `FRONTEND_HOST`              | Endereço permitido para CORS                           | `http://localhost:3000`                    |

### ⚛️ Frontend (Next.js)

Para o frontend, crie um arquivo `.env.local` na raiz da pasta `frontend/`. 

Exemplo:
```env
NEXT_PUBLIC_BACKEND_HOST=http://localhost:8080
```

## 📊 Diferença entre application.properties dev e prod

| Configuração        | `application.properties` (Dev)       | `application-prod.properties` (Prod) |
|:-------------------|:-------------------------------------|:-------------------------------------|
| `spring.jpa.show-sql` | `true` (exibe queries no console)    | `false` (não exibe)                  |
| `spring.jpa.hibernate.ddl-auto` | `update` (cria e atualiza tabelas) | `update` ou `validate`               |
| Credenciais e JWT    | via `.env` local                     | via variáveis do Railway ou servidor |
| Perfis ativos        | `dev`                                 | `prod`                               |

Para ativar, configure a seguinte variavel na aplication.properties do backend:

```properties
spring.profiles.active=prod
```

**OBS:** Se, apesar das configurações das variáveis de ambiente, não funcionar a execução da aplicação, é possivel editar essas credenciais diretamente no código nos seguintes arquivos.
FrontEnd: `frontend/services/api/apiCllient.ts`
BackEnd: Nos arquivos `application.properties` e `backend/src/main/java/com/estoqueige/estoqueige/configs/SecurityConfig.java`

## 📈 Uso

Após a instalação:

- Cadastre novos materiais
- Consulte o estoque
- Edite informações
- Registre entradas e saídas
- Acompanhe histórico de movimentações

## 🚀 Desenvolvimento Futuro

- Acesso público para consulta de produtos
- Permitir requisições públicas de materiais
- Relatórios em PDF e Excel
- Histórico detalhado de movimentações

## 📄 Licença

Programa desenvolvido para uso acadêmico, sem fins comerciais. Direitos autorais dos desenvolvedores e da **Universidade Federal do Sul e Sudeste do Pará**.

## 👥 Contribuidores

- [Gabriel Martins da Costa](https://github.com/GabrielMartins404)
- [Marcos Francisco](https://github.com/marcollas/)
