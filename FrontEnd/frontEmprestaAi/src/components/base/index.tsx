import { Image } from "react-native";

import{ styles} from "./styles";

export function Base(){
    return(
        <Image source={require("@/../assets/images/base.png")} style={styles.image} />
    )
}