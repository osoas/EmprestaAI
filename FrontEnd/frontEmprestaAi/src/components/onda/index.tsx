import { Image } from "react-native";

import{ styles} from "./style";

export function Onda(){
    return(
        <Image source={require("@/../assets/images/onda.png")} style={styles.onda} />
    )
}