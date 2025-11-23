import {View, Text, StyleSheet} from "react-native";

import {Button} from "@/components/buttons";
import {Logo} from "@/components/logo";
import { Input } from "@/components/inputs";
import {Onda} from "@/components/onda";

export default function Index() {
    return (
        <View style={styles.container}>
            <Logo/>
            <Text style={styles.title}>Registre-se e empresta ai!</Text>

            <Text style={styles.label}>Email</Text>
            <Input  placeholder="Seu Email"/>
            <Text style={styles.label}>CPF</Text>
            <Input placeholder="Insira seu Cpf"/>
            <Text style={styles.label}>Endereço</Text>
            <Input placeholder="Informe seu Endereço"/>
            <Text style={styles.label}>Senha</Text>
            <Input placeholder="Crie sua Senha"/>
            <Button title="Registrar"/>

            <Onda/>
        </View>
    )
}


export const styles = StyleSheet.create({
    container:{
        flex:1,
        justifyContent:"center",
        alignItems:'center',
        gap:20,
        padding:18,
        boxSizing:"border-box",
        
    },

    title:{
        width:227,
        height:65.05,
        textAlign:"center",
        fontSize:32,
        fontWeight:"bold",
        color:"#000000",
        marginBottom:24,
        
        
    },

    label:{
        alignSelf:"flex-start",
        fontSize:16,
        color:"#000000",
        marginBottom:-10,
        marginLeft:60,
    }

   
})