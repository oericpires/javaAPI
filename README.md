
# Concessionária APP

Aplicação Spring Boot para gerenciamento de uma concessionária de veículos, permitindo cadastro, consulta, atualização e deleção de usuários e carros, com diferentes níveis de acesso (administrador, vendedor e usuário comum).

## Estrutura de pastas
concessionariaAPP/
                    ├── ConcessionariaAppApplication.java
                    ├── config/
                    │   └── SecurityConfig.java
                    ├── controller/
                    │   ├── AdminController.java
                    │   ├── CarroController.java
                    │   ├── UsuarioController.java
                    │   ├── VendedorController.java
                    │   └── DTO/
                    │       ├── CarroDTO/
                    │       │   └── CadastroCarroDTO.java
                    │       └── UsuarioDTO/
                    │           ├── LoginDTO.java
                    │           └── RegistroDTO.java
                    ├── model/
                    │   ├── Carro.java
                    │   ├── Usuario.java
                    │   └── enuns/
                    │       └── Cargo.java
                    ├── repository/
                    │   ├── CarroRepository.java
                    │   └── UsuarioRepository.java
                    └── seeders/
                        └── DataSeeder.java

## Funcionalidades

- **Autenticação e Autorização:**  
  Login com geração de token JWT e controle de acesso por cargos (ADMINISTRADOR, VENDEDOR, USUÁRIO).

- **Gestão de Usuários:**
    - Cadastro, login, atualização e deleção de usuários.
    - Listagem de todos os usuários (apenas para administradores).
    - Alteração de cargo de usuários (apenas para administradores).

- **Gestão de Carros:**
    - Cadastro, atualização e deleção de carros (apenas para vendedores).
    - Listagem pública de todos os carros.
    - Consulta de carro por ID.

- **Seed de Dados:**  
  População automática do banco de dados com usuários e carros de exemplo ao iniciar a aplicação.

## Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Security (JWT)
- Maven
- H2 Database (padrão para testes)
- BCrypt para criptografia de senhas

## Como Executar

1. Clone o repositório.
2. Execute `mvn spring-boot:run`.
3. Acesse a API via `http://localhost:8080`.

## Usuários de Exemplo

- **Admin:**  
  Email: admin@email.com  
  Senha: 123

- **Vendedor:**  
  Email: vendedor@email.com  
  Senha: 123

- **Usuário:**  
  Email: usuario@email.com  
  Senha: 123

## Endpoints Principais

- `/login` — Login de usuário
- `/registrar` — Cadastro de usuário
- `/usuario` — Consultar, atualizar ou deletar usuário autenticado
- `/admin/usuarios` — Listar, atualizar cargo ou deletar usuários (admin)
- `/vendedor/carros` — Cadastrar, atualizar ou deletar carros (vendedor)
- `/carros` — Listar todos os carros
- `/carros/{id}` — Consultar carro por ID o usuario deve esta logado

## Consumo de API
Para consumir a API, utilize ferramentas como Postman ou Insomnia. Configure os headers para incluir o token JWT no Authorization header para acessar os endpoints protegidos. ou pelo frontend da aplicação, mas nao se esqueca de usar o live server para evitar problemas de CORS.

---
