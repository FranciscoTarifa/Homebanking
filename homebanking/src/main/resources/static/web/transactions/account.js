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
            nameSlice:"",
            lastNameSlice:"",
        }
    },
    created(){
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');('myParam');
        this.imprimirClientes()
        this.imprimirTransactions()
    },
    mounted(){
    },
    methods:{
        imprimirClientes(){
            axios.get("/api/clients/current")
            .then(respuesta => {
                this.cliente = respuesta.data
                this.cuentas = this.cliente.accounts.sort((a,b) => a.id - b.id)
                this.nameSlice = this.cliente.firstName.slice(0,1)
                this.lastNameSlice = this.cliente.lastName.slice(0,1)
                this.activeAccounts = this.cuentas.filter(a => a.accountActive)
                this.loans = this.cliente.loans.sort((a,b) => b.id - a.id)
                this.cards = this.cliente.cards.sort((a,b) => b.id - a.id)
            })
        },
        imprimirTransactions(){
            axios.get("/api/accounts/" + this.id)
            .then(respuesta => {
                this.cuentas2 = respuesta.data
                this.transactions = this.cuentas2.transactions
                this.normalizarfecha(this.transactions)
                this.fechaCard(this.cards)
            })
        },
        normalizarfecha(transactionsArray){
            transactionsArray.forEach(transaction =>{
                transaction.date = transaction.date.slice(0,10)
            })
        },
        fechaCard(cardArray){
            cardArray.forEach(card=>{
                card.fromDate= card.fromDate.slice(2,7)
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