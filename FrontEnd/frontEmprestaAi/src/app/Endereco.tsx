import {View, Text, StyleSheet} from "react-native";

import {Button} from "@/components/buttons";
import {Logo} from "@/components/logo";
import { Input } from "@/components/inputs";
import {Onda} from "@/components/onda";

export default function Index() {
    return (
        <View style={styles.container}>
            <Logo/>
            <Text style={styles.title}>Insira seu endereço</Text>

            <Text style={styles.label}>CEP</Text>
            <Input  placeholder="Insira seu CEP"/>

            <Text style={styles.label}>Estado</Text>
            <Input placeholder="Insira o estado em que reside"/>

            <Text style={styles.label}>Cidade</Text>
            <Input placeholder="Insira a sua cidade"/>

            <Text style={styles.label}>Municipio</Text>
            <Input placeholder="Insira seu municipio"/>

            <Text style={styles.label}>Numero</Text>
            <Input placeholder="Insira o núimero de sua casa"/>

            <Button title="Registrar Endereço"/>

            <Onda/>
        </View>
    )
}


export const styles = StyleSheet.create({
    container:{
        flex:1,
        justifyContent:"center",
        alignItems:'center',
        gap:18,
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
        marginBottom:50,
        
        
    },

    label:{
        alignSelf:"flex-start",
        fontSize:16,
        color:"#000000",
        marginBottom:-10,
        marginLeft:60,
    }

   
})