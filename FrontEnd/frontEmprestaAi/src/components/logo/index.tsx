import { Image } from "react-native";

import{ styles} from "./style";

export function Logo(){
    return(
        <Image source={require("@/../assets/images/logo.png")}  style={styles.image} /> 
    )
}