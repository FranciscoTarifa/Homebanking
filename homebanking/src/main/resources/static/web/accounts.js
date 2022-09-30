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
            })
        },
        createAccount(){
            axios.post('/api/clients/current/accounts',`clientEmail=${this.cliente.email}&accountType=${this.accountType}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(()=>Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Your account has been successfully created.',
                showConfirmButton: false,
                timer: 1500
            }))
            .then(()=>window.location.reload())
        },
        logout(){
            axios.post("/api/logout")
            .then(()=>{
                window.location.href="/web/index.html"
            })
        },
        deleteAccount(){
            Swal.fire({
                title: 'Are you sure?',
                showCancelButton: true,
                confirmButtonText: 'Delete',
                }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch("/api/clients/current/accounts", `number=${this.accountNumber}`)
                    .then(() => Swal.fire("Account deleted successfully","","success"))
                    .catch(error =>
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: `${error.response.data}`,
                        }))
                .then(()=>window.location.reload())
                }
                })
        },
        printTransfers(){
            this.fromDate = new Date(this.fromDate).toISOString()
            this.toDate = new Date(this.toDate).toISOString()
            axios.post("/api/transactions/filtered",{fromDate:`${this.fromDate}`,toDate:`${this.toDate}`,accountNumber:`${this.accountNumberPrint}`})
            .then(() =>
            Swal.fire({
            position: "center",
            icon: "success",
            title: "The PDF was successfully downloaded to your computer.",
            showConfirmButton: false,
            timer: 1500,
            }))
            .catch(error =>
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `${error.response.data}`,
                }))
            .then(()=>{
                window.location.reload()
            })
        },
    },
    computed:{
    },

}).mount('#app')