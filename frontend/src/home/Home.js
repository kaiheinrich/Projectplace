import React from "react";
import { useHistory } from "react-router-dom";
import MenuAppBar from "../navBar/NavBar";
import {makeStyles} from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import ButtonBase from "@material-ui/core/ButtonBase";
import AddProjectButton from "../addProjectButton/AddProjectButton";

const images = [
    {
        url: './images/team.png',
        title: 'Team members',
        height: '100%',
        linkto: '/profile'
    },
    {
        url: './images/idea.jpg',
        title: 'Project ideas',
        height: '100%',
        linkto: '/project'
    }
];

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'grid',
        gridTemplateRows: "1fr 1fr"
    },
    image: {
        position: 'relative',
        height: 200,
        [theme.breakpoints.down('xs')]: {
            width: '100% !important', // Overrides inline-style
            height: 100,
        },
        '&:hover, &$focusVisible': {
            zIndex: 1,
            '& $imageBackdrop': {
                opacity: 0.15,
            },
            '& $imageMarked': {
                opacity: 0,
            },
            '& $imageTitle': {
                border: '4px solid currentColor',
            },
        },
    },
    focusVisible: {},
    imageButton: {
        position: 'absolute',
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: theme.palette.common.white,
    },
    imageSrc: {
        position: 'absolute',
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
        backgroundSize: 'cover',
        backgroundPosition: 'center 40%',
    },
    imageBackdrop: {
        position: 'absolute',
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
        backgroundColor: theme.palette.common.black,
        opacity: 0.4,
        transition: theme.transitions.create('opacity'),
    },
    imageTitle: {
        position: 'relative',
        fontSize: "1.8em",
        padding: `${theme.spacing(2)}px ${theme.spacing(4)}px ${theme.spacing(1) + 6}px`,
    },
    imageMarked: {
        height: 3,
        width: 18,
        backgroundColor: theme.palette.common.white,
        position: 'absolute',
        bottom: -2,
        left: 'calc(50% - 9px)',
        transition: theme.transitions.create('opacity'),
    },
}));

export default function Home() {

    const history = useHistory();
    const classes = useStyles();

    return (
        <>
            <MenuAppBar pagename="Home" searchIsActive={false}/>
            <div className={classes.root}>
                {images.map((image) => (
                    <ButtonBase
                        onClick={() => history.push(`${image.linkto}`)}
                        focusRipple
                        key={image.title}
                        className={classes.image}
                        focusVisibleClassName={classes.focusVisible}
                        style={{
                            height: image.height,
                        }}
                    >
          <span
              className={classes.imageSrc}
              style={{
                  backgroundImage: `url(${image.url})`,
              }}
          />
                        <span className={classes.imageBackdrop} />
                        <span className={classes.imageButton}>
            <Typography
                component="span"
                variant="subtitle2"
                color="inherit"
                className={classes.imageTitle}
            >
              {image.title}
                <span className={classes.imageMarked} />
            </Typography>
          </span>
                    </ButtonBase>
                ))}
            </div>
            <AddProjectButton/>
        </>
    );
}