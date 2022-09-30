const { createApp } = Vue

  createApp({
    data() {
      return {
        clientList:[],
        valorFirst:"",
        valorLast:"",
        valorEmail:"",
      }
    },
    created(){
      this.imprimirClientes()
    },
    mounted(){

    },
    methods:{
      imprimirClientes(){
        axios.get('/api/clients/')
        .then((respuesta)=>{
            this.clientList = respuesta.data;
        })
      },
      agregarClientes(){
        axios.post('/rest/clients/',{
          firstName: this.valorFirst,
          lastName: this.valorLast,
          email: this.valorEmail,
        })
        .then((respuesta)=>{
          this.clientList.push(respuesta.data)
        })
      },
      postearClientes(){
        axios.post(clients)
        .then(this.imprimirClientes)
      },
      eliminarCliente(clienteElegido){
        axios.delete("/rest/clients/"+clienteElegido.id)
        .then(()=>{
          this.imprimirClientes()
        })
      },
      editarCliente(clienteElegido){
        let newEmail
        newEmail = prompt("Enter your now Email")
        cliente={
          email: newEmail,
        }
        axios.patch("/rest/clients/"+clienteElegido.id,cliente)
        .then(this.imprimirClientes)
      },
    },
    computed:{

    },

  }).mount('#app')