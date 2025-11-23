import {View, Text, StyleSheet} from "react-native";


import {Button} from "@/components/buttons";
import {Logo} from "@/components/logo";
import { Input } from "@/components/inputs";

import { Base } from "@/components/base";

export default function Index() {
    return (
        <View style={styles.container}>
            <Logo/>
            <Text style={styles.title}>Bem-vindo de volta!</Text>

            <Text style={styles.ponto}>.</Text>

            <Text style={styles.label}>Email</Text>
            <Input  placeholder="Seu Email"/>
            <Text style={styles.label}>Senha</Text>
            <Input placeholder="Crie sua Senha"/>
            <Button title="Entrar"/>

            <Base/>
        </View>
    )
}


export const styles = StyleSheet.create({
    container:{
        flex:0.9,
        justifyContent:"center",
        alignItems:'center',
        gap:20,
        padding:18,
        boxSizing:"border-box",
        
    },

    title:{
        width:220,
        height:65.05,
        textAlign:"center",
        fontSize:30,
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
    },

    ponto:{
        color:"#FFFFFF",
        padding:25,
    }

   
})