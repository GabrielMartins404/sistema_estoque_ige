const dotenv = require('dotenv')
const path = require('path')
// Carrega o .env de uma pasta acima
dotenv.config({ path: path.resolve(__dirname, '../.env') })

// Configuração do Next
module.exports = {
  env: {
    NEXT_PUBLIC_BACKEND_HOST: process.env.BACKEND_HOST
  },
}