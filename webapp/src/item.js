import React, { useEffect, useState } from "react";
import TextField from '@mui/material/TextField';

export default function InventoryItem(props) {
    const setQuantity = (value) => {
        console.log(props);
        props.item.quantity = value;
    };

    return (
        <TextField id="quantity" label="Quantity" type="number" fullWidth variant="standard" onChange={(e) => setQuantity(e.target.value)}/>
    )
}
