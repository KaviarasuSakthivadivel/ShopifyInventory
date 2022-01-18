import React, { useEffect, useState } from "react";
import { DataGrid } from '@mui/x-data-grid';
import axios from 'axios';

const columns = [
    {
        field: 'id', headerName: 'ID', width: 90
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
        editable: true,
        width: 160,
    },
];

export default function InventoryList() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);

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

    return (
        <div>
            {loading ? (
                <div style={{ height: '100%', width: '100%' }}>
                    <DataGrid
                        rows={items}
                        columns={columns}
                        rowsPerPageOptions={[100]}
                        disableSelectionOnClick
                        loading
                        stickyHeader
                    />
                </div>
            ) : (
                <>
                <div style={{ height: '800px', width: '100%' }}>
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
        </div>

    );
}
