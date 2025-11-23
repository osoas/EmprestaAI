import {View, FlatList, Text, StyleSheet} from 'react-native';

import {Input} from '@/components/inputs';


 export default function Home(){
    return(
        <View style={styles.container} >
        <Input placeholder='Pesquisar'  />
        </View>
    )
 }



export const styles = StyleSheet.create({
    container:{
        flex:1,
        alignItems:"center",
        justifyContent:"center",
        backgroundColor:"#fff",

 
    },
    
});
