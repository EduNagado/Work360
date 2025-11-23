# Criar o Resource Group
az group create --name work360-dev-rg --location brazilsouth

# Criar o servidor SQL dentro do Resource Group
az sql server create \
  --name work360-sqlsrv \
  --resource-group work360-dev-rg \
  --location brazilsouth \
  --admin-user admnagado \
  --admin-password "Dudu2004-" \
  --enable-public-network true

# Criar o banco de dados
az sql db create \
  --resource-group work360-dev-rg \
  --server work360-sqlsrv \
  --name work360_db \
  --service-objective Basic \
  --backup-storage-redundancy Local \
  --zone-redundant false

# Criar regra de firewall para permitir acesso externo
az sql server firewall-rule create \
  --resource-group work360-dev-rg \
  --server work360-sqlsrv \
  --name AllowAll \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255


----------------------TABELAS-----------------------------------

CREATE TABLE usuarios (
    id BIGINT IDENTITY PRIMARY KEY,
    nome NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) UNIQUE NOT NULL,
    senha NVARCHAR(255) NOT NULL
);

CREATE TABLE tarefas (
    id BIGINT IDENTITY PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    titulo NVARCHAR(255) NOT NULL,
    descricao NVARCHAR(MAX),
    prioridade NVARCHAR(50),
    estimativa FLOAT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE reunioes (
    id BIGINT IDENTITY PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    titulo NVARCHAR(255) NOT NULL,
    descricao NVARCHAR(MAX),
    data DATETIME2 NOT NULL,
    link NVARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE analytics_metrica (
    id BIGINT IDENTITY PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    data DATE,
    minutos_foco INT,
    minutos_reuniao INT,
    tarefas_concluidas_no_dia INT,
    periodo_mais_produtivo NVARCHAR(100),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE analytics_evento (
    id BIGINT IDENTITY PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tarefa_id BIGINT NULL,
    reuniao_id BIGINT NULL,
    tipo_evento NVARCHAR(100) NOT NULL,
    timestamp DATETIME2 NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (tarefa_id) REFERENCES tarefas(id),
    FOREIGN KEY (reuniao_id) REFERENCES reunioes(id)
);

CREATE TABLE relatorios (
    id BIGINT IDENTITY PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    tarefas_concluidas INT,
    tarefas_pendentes INT,
    reunioes_realizadas INT,
    minutos_foco_total INT,
    percentual_conclusao FLOAT,
    risco_burnout FLOAT,
    tendencia_produtividade NVARCHAR(100),
    tendencia_foco NVARCHAR(100),
    insights NVARCHAR(MAX),
    recomendacaoIA NVARCHAR(MAX),
    resumo_geral NVARCHAR(255),
    criado_em DATETIME2 DEFAULT SYSDATETIME(),
    relatorio_anterior_id BIGINT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (relatorio_anterior_id) REFERENCES relatorios(id)
);

