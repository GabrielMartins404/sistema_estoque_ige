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
- [Desenvolvimento Futuro](#desenvolvimento-futuro)
- [Licença](#licença)
- [Contribuidores](#contribuidores)

## 📌 Introdução

O **Estoque IGE** é um sistema desenvolvido para auxiliar na gestão de estoque de materiais institucionais. Construído como projeto acadêmico, ele explora tecnologias modernas para oferecer um ambiente funcional e de fácil uso, tanto para usuários administrativos quanto para a gestão acadêmica.

## 🎯 Contexto e Problema

A gestão através de planilhas de materiais em instituições de ensino superior pode gerar diversos problemas, como perdas de itens, dificuldade de rastreamento e falhas na reposição de estoque. Pensando nisso, este projeto surgiu com a proposta de informatizar e organizar esse processo no campus da **UNIFESSPA**, proporcionando mais segurança, controle e agilidade no gerenciamento dos materiais.

## ⚙️ Como Funciona

A aplicação é dividida em duas partes principais:

- **Backend**: desenvolvido em **Java**, utilizando o framework **Spring Boot** e o padrão de arquitetura **MVC (Model-View-Controller)**. 
Navegando até o caminho `src\main\java\com\estoqueige\estoqueige`, existirá nove pastas que fazem todo o gerenciamento de toda a administração do Banco de dados e BackEnd. Sendo essas:

 - `models`: Pasta responsável por armazenar as classes que representam as entidades do domínio da aplicação e os enums associados, correspondendo às tabelas no banco de dados.

 - `repositories`: Pasta que contém as interfaces responsáveis pelo acesso aos dados, geralmente estendendo JpaRepository ou CrudRepository, definindo as operações de CRUD e consultas customizadas.

 - `services`: Pasta onde ficam as classes de serviço, responsáveis por implementar as regras de negócio da aplicação, intermediando a comunicação entre os controllers e os repositórios.

 - `controllers`: Pasta que armazena as classes controladoras, responsáveis por mapear as requisições HTTP e direcioná-las para os serviços apropriados, retornando as respostas adequadas.

 - `dto`: Pasta onde são armazenadas as classes DTO (Data Transfer Object), utilizadas para trafegar dados entre o cliente e o servidor de forma estruturada, sem expor diretamente as entidades do banco.

 - `exceptions`: Pasta que contém as classes de tratamento de exceções customizadas, permitindo personalizar as respostas de erro da aplicação.

 - `configs`: Pasta onde ficam as classes de configuração da aplicação, como configurações de CORS, beans customizados e outras definições específicas do Spring.

 - `security`: Pasta responsável por armazenar as configurações e classes relacionadas à segurança da aplicação, como filtros de autenticação, configurações de WebSecurity, JWT ou OAuth2.

A estrutura do projeto conta com os arquivos padrões do Spring e uma pasta adicional chamada `views`, localizada na raíz do diretório, a qual contém a lógica do front end.
  
- **Frontend**: construído com **React**, **Next.js**, **TypeScript** e estilização com **Tailwind CSS**. O frontend se comunica com o backend via requisições HTTP, consumindo os serviços disponibilizados pela API.

A aplicação permite operações como:

- Cadastro e gerenciamento de usuários
- Definição de permissões e níveis de acesso
- Cadastro de produtos
- Consulta de itens em estoque
- Cadastro e gerenciamento de unidades e categorias de produtos
- Atualização de informações
- Registro de entrada e saída de materiais
- Cadastro e gerenciamento de requisitantes juntamente com seus cursos de origem

## 🛠️ Recursos

- Sistema web responsivo e moderno
- Backend com **Spring Boot** e arquitetura MVC
- Frontend em **React/Next.js** com **TypeScript** e **Tailwind CSS**
- API REST para integração entre frontend e backend
- Tela principal incorporando os arquivos do frontend na estrutura do backend
- Controle de estoque com cadastro, atualização e movimentação de materiais

## 📥 Instalação

### Pré-requisitos

- Java 17+
- Node.js 18+
- NPM ou Yarn
- MySQL (ou outro banco configurado no projeto)

### Passos para instalar

1. Clone o repositório:
   ```bash
   git clone https://github.com/GabrielMartins404/Estoque_IGE.git
   ```

2. Navegue até o arquivo `src\main\java\com\estoqueige\estoqueige\EstoqueIgeApplication.java` e o execute.

3. Na pasta do frontend, instale as dependências:
   ```bash
   npm install
   ```

4. Rode o frontend:
   ```bash
   npm run dev
   ```

5. Acesse a aplicação em: `http://localhost:3000`

OBS: A aplicação do backEnd rodará por padrão na porta `http://localhost:8080`. Além disso, é configurado no cors da aplicação para que só seja aceito requisições da porta 3000.


## 📥 Instalação por docker

Caso prefira, há a opção de rodar usando docker seguindo os seguintes passos.

### Pré-requisitos

- Docker instalado
- Docker-compose instalado

### Passos para instalar

1. Clone o repositório:
   ```bash
   git clone https://github.com/GabrielMartins404/Estoque_IGE.git
   ```

2. Navegue até o arquivo a raiz do projeto onde existe o arquivo `docker-compose.yml` e execute o comando:
`docker-compose up` para iniciar a aplicação.
`docker-compose down` para finalizar

## 📈 Uso

Após a instalação, acesse a aplicação para:

- Realizar cadastro de novos materiais
- Consultar estoque disponível
- Editar informações de itens existentes
- Registrar entradas e saídas de materiais no campus
- Acompanhar o histórico de movimentação
- E etc...

## 🚀 Desenvolvimento Futuro

- Permissão para requisitante não autenticados visualizarem o estoque de produto
- Permissão para requisitante não autenticados requisitarem produtos ao almoxarifado
- Relatórios em PDF e Excel
- Registro de histórico detalhado de movimentações

## 📄 Licença

Este projeto é de uso acadêmico e sem fins comerciais. Os direitos autorais pertencem aos desenvolvedores do projeto e à **Universidade Federal do Sul e Sudeste do Pará**.

## 👥 Contribuidores

- [Gabriel Martins da Costa](https://github.com/GabrielMartins404)
- [Marcos Francisco](https://github.com/marcollas/)