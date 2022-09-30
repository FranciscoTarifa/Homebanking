const { createApp } = Vue

createApp({
    data() {
        return {
            transactions:[],
            cliente:{},
            cuentas:[],
            cuentas2:[],
            loans:[],
            cards:[],
            id:"",
            queryString:"",
            params:"",
            activeCards:"",
            cardNumber:"",
            dateNow:[],
            dateActually:[],
            nameSlice:"",
            lastNameSlice:"",
            creditCards:[],
            debitCards:[],
        }
    },
    created(){
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');('myParam');
        this.imprimirClientes()
        this.imprimirTransactions()
        this.dateNow = Date.now();
        this.dateActually = new Date(this.dateNow).toISOString().slice(2,7)
        console.log(this.dateActually)
    },
    mounted(){
    },
    methods:{
        imprimirClientes(){
            axios.get("/api/clients/current")
            .then(respuesta => {
                this.cliente = respuesta.data
                this.cuentas = this.cliente.accounts
                this.loans = this.cliente.loans.sort((a,b) => b.id - a.id)
                this.cards = this.cliente.cards.sort((a,b) => b.id - a.id)
                this.nameSlice = this.cliente.firstName.slice(0,1)
                this.lastNameSlice = this.cliente.lastName.slice(0,1)
                this.activeCards = this.cards.filter(c => c.cardActive) 
                this.creditCards = this.activeCards.filter((c) => c.cardtype == 'CREDIT')
                this.debitCards = this.activeCards.filter((c) => c.cardtype == 'DEBIT')
                console.log(this.debitCards)
            })
        },
        imprimirTransactions(){
            axios.get("/api/accounts/" + this.id)
            .then(respuesta => {
                this.cuentas2 = respuesta.data
                this.transactions = this.cuentas2.transactions
                this.normalizarfecha(this.transactions)
            })
        },
        normalizarfecha(transactionsArray){
            transactionsArray.forEach(transaction =>{
                transaction.date = transaction.date.slice(0,10)
            })
        },
        logout(){
            axios.post("/api/logout")
            .then(()=>{
                window.location.href ="/web/index.html"
            })
        },
        deleteCard(){
            Swal.fire({
                title: 'Are you sure?',
                showCancelButton: true,
                confirmButtonText: 'Delete',
                }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch("/api/clients/current/cards",`number=${this.cardNumber}`)
                    .then(() => Swal.fire("Card deleted successfully","","success"))
                    .catch(error =>
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: `${error.response.data}`,
                        }))
                .then(()=>window.location.reload())
                }
                })
        }
    },
    computed:{
    },

}).mount('#app')