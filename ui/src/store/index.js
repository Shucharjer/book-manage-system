// src/store/index.js
import { defineStore } from 'pinia';

export const useStore = defineStore('general', {
    state: () => ({
        reader: {
            id: 0,
            name: '',
            sex: '',
            type: 0,
            dept: '',
            phone: '',
            email: '',
            registerTime:'',
            photo:'',
            status:'',
            borrowNum: 0,
            role: 0,
            password:'',

        },
    }),

});


