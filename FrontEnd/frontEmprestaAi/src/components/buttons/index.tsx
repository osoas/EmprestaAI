import { TouchableOpacity, TouchableOpacityProps, Text } from "react-native";
import {styles} from "./style";

export function Button({title, ...rest} : TouchableOpacityProps & {title:string}){
    return(
        <TouchableOpacity style={styles.button} {...rest}>
            <Text style={styles.title}>{title}</Text>
        </TouchableOpacity>
    )
}