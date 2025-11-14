import React from "react";
import {View, Text, TextInput, StyleSheet, TouchableOpacity, Image, ScrollView} from "react-native";
import {MaterialIcons} from "@expo/vector-icons";

export default function RegisterScreen(){
    return(
        <ScrollView contentContainerStyle={styles.container}>
        <Image
            source={require("../assets/images/header.png")}
            style={styles.headerImage}
            resizeMode="cover"
        />

        <Text style={styles.title}>Registre-se e empresta aí!</Text>

        <View style={styles.form}>

        <Text style={styles.label}>Email</Text>
        <TextInput style={styles.input} placeholder = "Seu Email" placeholderTextColor="#A9A9A9" />

        <Text style={styles.label}>Cpf</Text>
        <TextInput style={styles.input} placeholder="Insira seu CPF" placeholderTextColor="#A9A9A9" />

         <Text style={styles.label}>Endereço</Text>
         <View style={styles.inputIconContainer}>
         <TextInput
                style={styles.inputIcon}
                placeholder={"Informe seu Endereço"}
                placeholderTextColor="A9A9A9"
                />

         <MaterialIcons name="location-pin" size={22} color="#555"/>
         </View>

         <Text style={styles.label}>Senha</Text>
         <TextInput
                style={styles.input}
                placeholder="Crie sua Senha"
                secureTextEntry
                placeholderTextColor="A9A9A9"
                />

         <TouchableOpacity style={styles.button}>
            <Text style={styles.buttonText}>Registrar</Text>
            </TouchableOpacity>
          </View>

          <Image
                source={require("../assets/images/onda.png")}
                style={styles.onda}
                resizeMode="cover"
                />
        </ScrollView>
        );


    }


    const styles = StyleSheet.create({
        container: {
            alignItems: "center",
            paddingBottom: 40,
            backgroundColor: "#F5F5FF"
         },


        headerImage: {
            width: "50%",
            height: 110,
            marginBottom:10,
            },



        title:{
            fontSize:24,
            fontWeight:"bold",
            textAlign:"center",
            marginBottom:25,
            color:"#000",
            },

        form:{
            width:"60%",
            },

        label:{
            marginBottom:4,
            color:"#333",
            fontWeight:"500",
            fontSize:14,

            },

        input:{
            backgroundColor:"#F1F1F1",
            padding:12,
            borderRadius:20,
            marginBottom:15,
            },

        inputIconContainer:{
            backgroundColor:"#F1F1F1",
            flexDirection:"row",
            alignItems:"center",
            paddingHorizontal:12,
            borderRadius:20,
            marginBottom:18,

            },

        inputIcon:{
            flex:1,
            paddingVertical:12,
            fontSize:15,
            },

        button:{
            backgroundColor:"#000",
            paddingVertical:14,
            borderRadius:30,
            alignItems:"center",
            marginTop:10,

            },

        buttonText:{
            color:"#fff",
            fontSize:17,
            fontWeight:"600",

            },

        onda:{
            width:"50%",
            height:70,
            marginTop:10,
            },







        });