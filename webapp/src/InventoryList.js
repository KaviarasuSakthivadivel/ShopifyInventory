import React, { useEffect, useState } from "react";
import { DataGrid } from '@mui/x-data-grid';
import Fab from '@mui/material/Fab';
import Box from '@mui/material/Box';
import AddIcon from '@mui/icons-material/Add';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Skeleton from '@mui/material/Skeleton';
import Stack from '@mui/material/Stack';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import InventoryItem from './item.js';
import Snackbar from '@mui/material/Snackbar';

import axios from 'axios';

const columns = [
    {
        field: 'id', headerName: 'ID', width: 300
    },
    {
        field: 'name',
        headerName: 'Item name',
        width: 150,
        editable: true,
    },
    {
        field: 'cost_price',
        headerName: 'Cost Price',
        type: 'number',
        width: 150,
        editable: true,
    },
    {
        field: 'selling_price',
        headerName: 'Selling Price',
        type: 'number',
        width: 110,
        editable: true,
    },
    {
        field: 'stock',
        headerName: 'Stock',
        type: 'number',
        editable: true,
        width: 160,
    }
];

export default function InventoryList() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [open, setOpen] = useState(false);
    const [itemName, setItemName] = useState("");
    const [sellingPrice, setSellingPrice] = useState();
    const [costPrice, setCostPrice] = useState();
    const [stock, setStock] = useState();
    const [shipmentModelOpen, setShipmentModelOpen] = useState(false);
    const [customerName, setCustomerName] = useState("");
    const [courierName, setCourierName] = useState("");
    const [trackingNo, setTrackingNo] = useState("");
    const [message, setMessage] = useState("");
    const [toastOpen, setToastOpen] = useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleDeleteClick = (id) => {
        console.log(id);
    };

    const handleSetToastClose = () => {
        setToastOpen(false);
    };

    const url = "http://localhost:8080";

    useEffect(() => {
        getInventoryList();
    }, []);

    const getInventoryList = () => {
        axios.get(`${url}/api/inventory`).then((response) => {
            console.log(response);

            setLoading(false);
            setItems(response.data);
        }).catch(error => {
            console.error(`Error occurred, ${error}`);
        });
    };

    const onTableCellEditCommit = (item) => {
        console.log(item);
        const requestData = {};
        requestData[item.field] = item.value;
        axios.put(`${url}/api/inventory/${item.id}`, requestData).then((response) => {
            console.log(response);
        }).catch(error => {
            console.error(`Error occurred, ${error}`);
        });
    };

    const handleInventoryCreate = () => {
        const requestData = {'name': itemName, 'selling_price': sellingPrice, 'cost_price': costPrice, 'stock': stock};
        console.log(requestData);
        axios.post(`${url}/api/inventory`, requestData).then((response) => {
            console.log(response);
            getInventoryList();
            handleClose();
        }).catch(error => {
            console.error(`Error occurred, ${error}`);
        });
    };

    const handleShipmentModelClose = () => {
        setShipmentModelOpen(false);
    };

    const handleShipmentCreate = () => {
        console.log(items);
        const requestData = {'customer_name': customerName, 'courier_name': courierName, 'tracking_no': trackingNo};
        const inventories = [];

        items.forEach(items => {
            if(items.quantity && items.quantity !== "0") {
                inventories.push({'id': items.id, 'stock' : items.quantity});
            }
        });

        if(inventories.length > 0) {
            requestData['inventories'] = inventories;
        }

        axios.post(`${url}/api/shipment`, requestData).then((response) => {
            if(response.data === "success") {
                setMessage("Shipment created");
                setToastOpen(true);
                getInventoryList();
                handleShipmentModelClose();
            } else {
                setMessage(response.data);
                setToastOpen(true);
            }
        }).catch(error => {
            console.error(`Error occurred, ${error}`);
        });

    };

    const handleSetShipmentModelOpen = () => {
        setShipmentModelOpen(true);
    };

    const elements = ['one', 'two', 'three'];

    return (
        <Box>
            {loading ? (
                <Box sx={{ width: 300 }}>
                    <Skeleton />
                    <Skeleton animation="wave" />
                    <Skeleton animation={false} />
                </Box>
            ) : (
                <>
                <div style={{ height: '500px', width: '100%' }}>
                    <DataGrid
                        rows={items}
                        columns={columns}
                        rowsPerPageOptions={[100]}
                        disableSelectionOnClick
                        stickyHeader
                        onCellEditCommit={onTableCellEditCommit}
                    />
                </div>
                </>
            )}
            <Box sx={{ '& > :not(style)': { m: 1 } }} style={{justifyContent: "flex-end", display: "flex"}}>
                <Fab color="primary" aria-label="add" onClick={handleClickOpen}>
                    <AddIcon />
                </Fab>

                <Fab variant="extended" onClick={handleSetShipmentModelOpen}>
                    <LocalShippingIcon sx={{ mr: 1 }} />
                    Create Shipment
                </Fab>
            </Box>

            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Create Inventory Item</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To subscribe to this website, please enter your email address here. We
                        will send updates occasionally.
                    </DialogContentText>
                    <Box component="form">
                        <Stack spacing={2} sx={{ width: 500 }}>
                            <TextField autoFocus id="name" label="Name" type="text" fullWidth variant="standard" onChange={(e) => setItemName(e.target.value)} />
                            <TextField id="cost_price" label="Cost Price" type="text" fullWidth variant="standard" onChange={(e) => setCostPrice(e.target.value)} />
                            <TextField id="selling_price" label="Selling Price" type="text" fullWidth variant="standard" onChange={(e) => setSellingPrice(e.target.value)} />
                            <TextField id="stock" label="Stock" type="text" fullWidth variant="standard" onChange={(e) => setStock(e.target.value)} />
                        </Stack>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleInventoryCreate}>Create</Button>
                </DialogActions>
            </Dialog>


            <Dialog open={shipmentModelOpen} onClose={handleShipmentModelClose} fullScreen>
                <DialogTitle>Create Shipment</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Assign inventory and create shipment here.
                    </DialogContentText>
                    <Box component="form">
                        <Stack spacing={2} sx={{ width: 500 }}>
                            <TextField autoFocus id="customer_name" label="Customer Name" type="text" fullWidth variant="standard" onChange={(e) => setCustomerName(e.target.value)} />
                            <TextField id="cost_price" label="Courier Name" type="text" fullWidth variant="standard" onChange={(e) => setCourierName(e.target.value)} />
                            <TextField id="selling_price" label="Tracking No" type="text" fullWidth variant="standard" onChange={(e) => setTrackingNo(e.target.value)} />
                            <div>
                            Select items
                            </div>
                            <ul>
                                {items.map((value, index) => {
                                    return (
                                        <li key={index}>{value.name}
                                            <InventoryItem item={value} />
                                        </li>
                                    )
                                })}
                            </ul>

                        </Stack>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleShipmentModelClose}>Cancel</Button>
                    <Button onClick={handleShipmentCreate}>Create</Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={toastOpen} autoHideDuration={6000} onClose={handleSetToastClose} message={message} />
        </Box>
    );
}
