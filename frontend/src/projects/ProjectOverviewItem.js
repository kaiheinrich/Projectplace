import React from "react";
import {useHistory} from "react-router-dom";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import makeStyles from "@material-ui/core/styles/makeStyles";
import {CardHeader} from "@material-ui/core";

export default function ProjectOverviewItem({project}) {

    const classes = useStyles();
    const history = useHistory();

    return (
        <Card className={classes.root} variant="elevation" elevation={20}>
            <CardHeader
                className={classes.cardHeader}
                title={project.title}
                subheader={<Typography>by {project.projectOwner}</Typography>}/>
            <CardContent className={classes.teaser}>
                <Typography variant="body2" component="p">
                    {project.teaser}
                </Typography>
            </CardContent>
            <CardContent>
                {project.imageUrl && <img width="100%" alt="project" src={project.imageUrl}/>}
            </CardContent>
            <CardActions className={classes.buttonPosition}>
                <Button className={classes.button} size="small" onClick={() => history.push(`/project/${project.id}`)}>Check out project</Button>
            </CardActions>
        </Card>
    );
}

const useStyles = makeStyles({
    root: {
        minWidth: 275,
        backgroundColor: "#FFFFFF",
        borderRadius: "5px"
    },
    cardHeader: {
        backgroundColor: "#F3EED9"
    },
    button: {
        backgroundColor: "#e7e7e7",
        color: "black",
        width: "max-content",
        borderRadius: "10px",
        fontSize: "0.9em",
        padding: "8px"
    },
    name: {
        fontSize: 20
    },
    teaser: {
        paddingBottom: "0"
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
    buttonPosition: {
        justifyContent: "center",
        alignContent: "center",
        padding: "0px 16px 16px 16px"
    }
});