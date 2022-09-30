const { createApp } = Vue

createApp({
    data() {
        return {
            transactions:[],
            cliente:{},
            cuentas:[],
            cuentas2:[],
            loans:[],
            id:"",
            queryString:"",
            params:"",
            activeAccounts:"",
            accountNumber:"",
            accountType:"",
            fromDate:"",
            toDate:"",
            accountNumberPrint:"",
            nameSlice:"",
            lastNameSlice:"",
            accountSelected:"",
            cardHolderSelected:"",
            cardNumberSelected:"",
            amountSelected:"",
            cvvSelected:"",
            thruDateSelected:"",
            descriptionSelected:"",
        }
    },
    created(){
        this.imprimirClientes()
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
                this.activeCards = this.cards.filter(c => c.cardActive) 
                console.log(this.activeCards)
            })
        },
        makePayment() {
            Swal.fire({
            title: 'Are you sure?',
            showCancelButton: true,
            confirmButtonText: 'Payment',
            }).then((result) => {
            if (result.isConfirmed) {
                axios.post('/api/transactions/payment', {cardNumber:this.cardNumberSelected,cardCvv:this.cvvSelected,amount:this.amountSelected,description:this.descriptionSelected,thruDate:this.thruDateSelected,cardHolder:this.cardHolderSelected,number:this.accountSelected})
                .then(() => Swal.fire("Successful payment","","success"))
                .then(()=>window.location.href="./accounts.html")
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${error.response.data}`,
                    }))
            }
            })
        },
        logout() {
            axios.post("/api/logout").then(() => {
                window.location.href = "/web/index.html";
            });
        },
    },
    computed:{
    },

}).mount('#app')