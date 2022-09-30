const { createApp } = Vue

createApp({
    data() {
        return {
            transactions:[],
            cliente:{},
            cuentas:[],
            cuentas2:[],
            transferType:"",
            id:"",
            queryString:"",
            params:"",
            amount:"",
            description:"",
            numberAccountOrigin:"",
            numberAccountReceive:"",
            nameSlice:"",
            lastNameSlice:"",
            activeAccounts:"",
        }
    },
    created(){
    /*  this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');('myParam'); */
        this.imprimirClientes()
        /* this.imprimirTransactions() */
    },
    mounted(){
    },
    methods:{
        imprimirClientes(){
            axios.get("/api/clients/current")
            .then(respuesta => {
                this.cliente = respuesta.data
                this.cuentas = this.cliente.accounts
                this.nameSlice = this.cliente.firstName.slice(0,1)
                this.lastNameSlice = this.cliente.lastName.slice(0,1)
                this.loans = this.cliente.loans.sort((a,b) => b.id - a.id)
                this.cards = this.cliente.cards.sort((a,b) => b.id - a.id)
                this.activeAccounts = this.cuentas.filter(a => a.accountActive)
            })
        },
/*         imprimirTransactions(){
            axios.get("/api/accounts/" + this.id)
            .then(respuesta => {
                this.cuentas2 = respuesta.data
                this.transactions = this.cuentas2.transactions
            })
        }, */
        realizarTransaccion(){
            numberAccountReceive = this.numberAccountReceive.toUpperCase()
            Swal.fire({
                title: 'Are you sure?',
                showCancelButton: true,
                confirmButtonText: 'Confirm',
                }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("/api/transactions",`amount=${this.amount}&description=${this.description}&numberAccountOrigin=${this.numberAccountOrigin}&numberAccountReceive=${numberAccountReceive}`,{headers: { "content-type": "application/x-www-form-urlencoded"},})
                    .then(() => Swal.fire("The transaction has been carried out successfully","","success"))
                    .then((response)=>{
                        window.location.href="/web/accounts.html"
                    })
                    .catch(error =>
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: `${error.response.data}`,
                        }))
                }
                })
        },
        logout(){
            axios.post("/api/logout")
            .then(()=>{
                window.location.href ="/web/index.html"
            })
        },
    },
    computed:{
    },

}).mount('#app')