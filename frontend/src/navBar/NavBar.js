import React, {useContext, useState} from 'react';
import {useHistory} from "react-router-dom";
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import MessageIcon from '@material-ui/icons/Message';
import SearchIcon from '@material-ui/icons/Search';
import AccountCircle from '@material-ui/icons/AccountCircle';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import UserContext from "../contexts/UserContext";
import InputBase from "@material-ui/core/InputBase";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    appBar: {
        backgroundColor: "#9f0d2f",
        fontFamily: "arial"
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
    searchField: {
        display: "block",
        backgroundColor: "white",
        padding: "2px 4px"
    }
}));

export default function MenuAppBar(props) {
    const classes = useStyles();
    const [anchorEl, setAnchorEl] = useState(null);
    const openProfileMenu = Boolean(anchorEl);
    const [mainAnchorEl, setMainAnchorEl] = useState(null);
    const openMainMenu = Boolean(mainAnchorEl);
    const [headerAction, setHeaderAction] = useState("");
    const history = useHistory();
    const {userCredentials, logout} = useContext(UserContext);

    const handleProfileMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleProfileMenuClose = () => {
        setAnchorEl(null);
    };

    const handleMainMenu = (event) => {
        setMainAnchorEl(event.currentTarget);
    };

    const handleMainMenuClose = () => {
        setMainAnchorEl(null);
    };

    const handleGoToHome = () => {
        history.push(`/home`);
        setMainAnchorEl(null);
    }

    const handleGoToProfileOverview = () => {
        history.push(`/profile`);
        setMainAnchorEl(null);
    }

    const handleGoToProjectOverview = () => {
        history.push(`/project`);
        setMainAnchorEl(null);
    }

    const handleGoToProfile = () => {
        history.push(`/profile/${userCredentials.sub}/edit`);
        setAnchorEl(null);
    }

    const handleLogout = () => {
        logout();
        history.push("/login");
        setAnchorEl(null);
    }

    const handleSearchBar = () => {
        headerAction ? setHeaderAction("") : setHeaderAction("search");
    }

    return (
        <div className={classes.root}>
            <AppBar className={classes.appBar} position="fixed">
                <Toolbar>
                    {(
                    <div>
                        <IconButton
                            edge="start"
                            className={classes.menuButton}
                            color="inherit"
                            aria-label="menu"
                            aria-controls="main-menu"
                            aria-haspopup="true"
                            onClick={handleMainMenu}
                        >
                            <MenuIcon />
                        </IconButton>
                        <Menu
                            id="main-menu"
                            open={openMainMenu}
                            anchorEl={mainAnchorEl}
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                        onClose={handleMainMenuClose}>
                            <MenuItem onClick={handleGoToHome}>Home</MenuItem>
                            <MenuItem onClick={handleGoToProfileOverview}>Profiles</MenuItem>
                            <MenuItem onClick={handleGoToProjectOverview}>Projects</MenuItem>
                        </Menu>
                    </div>
                    )}
                    <Typography variant="h6" className={classes.title}>
                        {props.pagename}
                    </Typography>
                    {props.searchIsActive === true && <IconButton
                        color="inherit"
                        onClick={handleSearchBar}>
                        <SearchIcon/>
                    </IconButton>}
                    <IconButton
                        color="inherit"
                        onClick={() => history.push(`/profile/${userCredentials.sub}/messages`)}>
                        <MessageIcon/>
                    </IconButton>
                    {(
                    <div>
                        <IconButton
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleProfileMenu}
                            color="inherit"
                        >
                            <AccountCircle />
                        </IconButton>
                        <Menu
                            id="menu-appbar"
                            anchorEl={anchorEl}
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            open={openProfileMenu}
                            onClose={handleProfileMenuClose}
                        >
                            <MenuItem onClick={handleGoToProfile}>Profile</MenuItem>
                            <MenuItem onClick={handleLogout}>Logout</MenuItem>
                        </Menu>
                    </div>
                    )}
                </Toolbar>
            </AppBar>
            <Toolbar/>
            {headerAction === "search" && <InputBase
                className={classes.searchField}
                type="text"
                placeholder="Search for skills..."
                value={props.searchTerm}
                onChange={event => props.setSearchTerm(event.target.value)}/>}
        </div>
    );
}
